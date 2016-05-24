#include <SoftwareSerial.h>
#include <Servo.h>
#include <EEPROM.h>
#include <string.h>

#define MON (1<<0)
#define TUE (1<<1)
#define WED (1<<2)
#define THU (1<<3)
#define FRI (1<<4)
#define SAT (1<<5)
#define SUN (1<<6)

#define MAGIC_EEPROM_INITIALIZED 42

#define ADDR_MAGIC_EEPROM_INITIALIZED 0x000
#define ADDR_MOTOR_POSITION 0x001
#define ADDR_SCHED 0x003

#define SCHED_NMEMB 128

struct sched_entry {
  byte days_active; /* 0xFF means this is the last entry */
  byte time_of_day; /* 0 - 47 */
  unsigned int motor_val; /* 0 - 65535 */
} sched_cache[SCHED_NMEMB];


SoftwareSerial mySerial(2, 3); // RX, TX

int sched_cache_used = 0;

Servo myservo;
bool stop = true;

bool awake = false;

int timer;
char cmd_buffer[128];

unsigned long last_time;
int hhsm = 0x420f; /* half hours since monday 00:00. rolls over to 0 every monday 00:00 */
/* 0x420f means it hasn't been initialized. */
/* NO DST HANDLING! */

void initialize_eeprom()
{
  byte state = EEPROM.read(ADDR_MAGIC_EEPROM_INITIALIZED);
  if(state != MAGIC_EEPROM_INITIALIZED){
    EEPROM.update(ADDR_SCHED, 0xFF);
    EEPROM.update(ADDR_MAGIC_EEPROM_INITIALIZED, MAGIC_EEPROM_INITIALIZED);
    EEPROM.update(ADDR_MOTOR_POSITION, 0);
    
  }else{ /* EEPROM has been previously initialized */
    for(int i = 0; i < SCHED_NMEMB; ++i){
      /* for ith entry of schedule list in EEPROM */
      byte days_active = EEPROM.read(i*sizeof(struct sched_entry));
      int eeprom_offs = i*sizeof(struct sched_entry)+ADDR_SCHED;

      /*
      sched_cache[i].days_active = EEPROM.read(eeprom_offs);
      sched_cache[i].time_of_day = EEPROM.read(eeprom_offs+1);
      sched_cache[i].motor_val = EEPROM.read(eeprom_offs+2) | (EEPROM.read(eeprom_offs+3) << 8);
      */

      for(int j = 0; j < 4; ++j){
        ((byte*)sched_cache)[eeprom_offs-ADDR_SCHED+j] = EEPROM.read(eeprom_offs+j);
      }
      
      if(days_active == 0xff){
        break;
      }
      sched_cache_used++;
    }
  }
}

void setup() {
  memset(sched_cache, 0, sizeof(sched_cache));
  // Open serial communications and wait for port to open:
  pinMode(2, INPUT);
  pinMode(3, OUTPUT);
  Serial.begin(9600);
  mySerial.begin(9600);

  myservo.attach(8);
  myservo.write(94);

  timer = 1;
  // set the data rate for the SoftwareSerial port
}

void loop() { // run over and over

  /* mobile sending info to arduino */
  if (mySerial.available()) {
    parser();
  }

  /* This can be used to send info to mobile */
  if (Serial.available()) {
    char num = Serial.read();
    //Serial.println(num);
    //mySerial.write(Serial.read());
    //mySerial.write(num);
    Serial.write(num);
  }
  
  if(hhsm != 0x420f && (unsigned long)(millis() - last_time) >= 1800000L){
    hhsm++;
    if(hhsm == 336){
      hhsm = 0;
    }
  }
  last_time = millis();
 
}

void sched_add_entry(byte days_active, byte hour, byte minute, unsigned int motorval)
{
  if(minute != 30 || minute != 00){
    Serial.println("ERROR: wrong value for minute");
  }
  if(hour > 23){
    Serial.println("ERROR: wrong value for hour");
  }
  if(sched_cache_used > 127){
    Serial.println("ERROR: sched_add_entry: too many elements on the schedule list");
  }
  sched_cache[sched_cache_used].days_active = days_active;
  sched_cache[sched_cache_used+1].days_active = 0xff;
  sched_cache[sched_cache_used].time_of_day = hour*2 + minute/30;
  sched_cache[sched_cache_used].motor_val = motorval;

  int eeaddr = ADDR_SCHED + sched_cache_used*sizeof(struct sched_entry);
  for(int i = 0; i < 4; ++i){
    EEPROM.update(eeaddr+i, ((byte*)sched_cache)[eeaddr-ADDR_SCHED + i]);
  }
  struct sched_entry test;
  for(int i = 0; i < 4; ++i){
    ((byte*)&test)[i] = EEPROM.read(eeaddr+i);
  }
  Serial.print("wrote days_active ");
  Serial.print(test.days_active);
  Serial.print(" time of day ");
  Serial.print(test.time_of_day);
  Serial.print(" motor value ");
  Serial.println(test.motor_val);
  
  EEPROM.update(eeaddr+4, 0xff);
  sched_cache_used++;
}
void resched()
{
  sched_cache_used = 0;
  sched_cache[0].days_active = 0xff;
  EEPROM.update(ADDR_SCHED, 0xFF);
}

void parser()
{
  byte nread = mySerial.readBytesUntil('\n', cmd_buffer, 127);
  cmd_buffer[nread] = '\0';
  char *cmd_buf = cmd_buffer;
  char *cmd = strsep(&cmd_buf, " ");
  if(strcmp(cmd, "RESCHED") == 0){
    resched();
    return;
  }
  if(cmd_buf == NULL){
    Serial.print("ERROR: Unknown single token command <");
    Serial.print(cmd);
    Serial.println('>');
    return;
  }
  if(strcmp(cmd, "SCHED") == 0){
    char *day = strsep(&cmd_buf, "|");
    
    /* at present this is hour:minute, but another call to strsep will make this the minute. */
    char *minute = strsep(&cmd_buf, "|");
    char *motorval = cmd_buf;
    char *hour = strsep(&minute, ":");
    byte h = (byte)atoi(hour);
    byte m = (byte)atoi(minute);
    unsigned int mv = (unsigned int)atoi(motorval);
    byte days_active = 0;
    
    if(strcmp("MON", day) == 0){
      days_active = MON;
    }else if(strcmp("TUE", day) == 0){
      days_active = TUE;
    }else if(strcmp("WED", day) == 0){
      days_active = WED;
    }else if(strcmp("THU", day) == 0){
      days_active = THU;
    }else if(strcmp("FRI", day) == 0){
      days_active = FRI;
    }else if(strcmp("SAT", day) == 0){
      days_active = SAT;
    }else if(strcmp("SUN", day) == 0){
      days_active = SUN;
    }

    sched_add_entry(days_active, h, m, mv);
  }
  if(strcmp(cmd, "SETTIME") == 0){
    
  }
}
