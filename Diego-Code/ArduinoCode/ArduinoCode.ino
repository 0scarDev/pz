#include <SoftwareSerial.h>
#include <Servo.h>
SoftwareSerial mySerial(2, 3); // RX, TX

Servo myservo;
bool stop = true;

bool awake = false;

int timer;
char mybuffer[100];
void setup() {
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
  /*  --- THIS IS FOR TIMER ALARM --- NOT SURE IF IT'S GOOD.. 
Serial.println(timer);
if (timer%10 == 0){
  myservo.write(150);
  delay(500);
  myservo.write(94);
}
  
timer++;
  delay(1000);
*/

}

void parser(){

String str_value = "";
mySerial.readBytes(mybuffer, 4);
delay(5);
while(mySerial.available()){
  str_value += (char)mySerial.read();
delay(5);
}
char *char_value = &str_value[0]; // converto to char array
int value = atoi(char_value); // convert to integer

if (strcmp(&mybuffer[0], "SERV") == 0){
  myservo.write(value);
}
else if (strcmp(&mybuffer[0], "LIGH") == 0){
  Serial.print("LIGHT TYPE: ");
  Serial.println(value);
}
else if (strcmp(&mybuffer[0], "PASS") == 0){
  Serial.print("PASS TYPE: ");
  Serial.println(value);
}
Serial.println("ALL DATA PARSED.");

}


