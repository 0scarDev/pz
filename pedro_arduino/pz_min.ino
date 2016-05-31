#include <SoftwareSerial.h>
#include <Servo.h>

SoftwareSerial esp8266(2,3); // make RX Arduino line is pin 2, make TX Arduino line is pin 3.
                             // This means that you need to connect the TX line from the esp to the Arduino's pin 2
                             // and the RX line from the esp to the Arduino's pin 3
Servo servo;
char cmd_buffer[128];
void setup()
{
  Serial.begin(9600);
  esp8266.begin(9600); // your esp's baud rate might be different
  servo.attach(7);
  delay(3);
  esp8266.print("AT\r\n");
  esp8266.print("AT+CIPMUX=1\r\n");
  esp8266.print("AT+CIPSERVER=1,1001\r\n");
}
 
void loop() { // run over and over
  if (esp8266.available()) {
    parser();
  //Serial.write(esp8266.read());
  }
  if (Serial.available()) {
    esp8266.write(Serial.read());
  }
}

void cccmd_parser(char *);

void parser()
{
  byte nread = esp8266.readBytesUntil('\n', cmd_buffer, 127);
  cmd_buffer[nread] = '\0';
  char *findcr = strchr(cmd_buffer, '\r');
  if(findcr != NULL){
    *findcr = '\0';
  }
  char *cmd_buf = cmd_buffer;
  Serial.println(cmd_buf);
  if(strncmp(cmd_buf, "+IPD,", 4)){
    strsep(&cmd_buf, ":");
    if(cmd_buf != NULL){
      cccmd_parser(cmd_buf);
    }else{
      Serial.println("ERROR: something wrong with IPD...");
    }
  }
}

void cccmd_parser(char *cmd_buf)
{
  Serial.print("wifi RECV COMMAND<");
  Serial.print(cmd_buf);
  Serial.println(">");
  char *cmd = strsep(&cmd_buf, " ");
  Serial.print("command arguments <");
  Serial.print(cmd_buf);
  Serial.println(">");
  Serial.print("command <"); Serial.print(cmd); Serial.println(">");
  if(strcmp(cmd, "RESCHED") == 0){
    return;
  }
  if(cmd_buf == NULL){
    Serial.print("ERROR: Unknown single token command <");
    Serial.print(cmd);
    Serial.println('>');
    return;
  }
  if(strstr(cmd, "SERV") != NULL){
    servo.write(atoi(cmd_buf));
    Serial.print("WROTE TO SERVO ");
    Serial.println(atoi(cmd_buf));
  }
}
