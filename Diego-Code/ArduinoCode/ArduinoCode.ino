/*
  Software serial multple serial test

 Receives from the hardware serial, sends to software serial.
 Receives from software serial, sends to hardware serial.

 The circuit:
 * RX is digital pin 10 (connect to TX of other device)
 * TX is digital pin 11 (connect to RX of other device)

 Note:
 Not all pins on the Mega and Mega 2560 support change interrupts,
 so only the following can be used for RX:
 10, 11, 12, 13, 50, 51, 52, 53, 62, 63, 64, 65, 66, 67, 68, 69

 Not all pins on the Leonardo support change interrupts,
 so only the following can be used for RX:
 8, 9, 10, 11, 14 (MISO), 15 (SCK), 16 (MOSI).

 created back in the mists of time
 modified 25 May 2012
 by Tom Igoe
 based on Mikal Hart's example

 This example code is in the public domain.

 */
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


