/*
 * Hello World!
 *
 * This is the Hello World! for Arduino. 
 * It shows how to send data to the computer
 */
long i;

void setup()                    // run once, when the sketch starts
{
  Serial.begin(115200);           // set up Serial library at 115200 bps
  
  Serial.println("BEGIN");  // prints hello with ending line break 
  i = 0;
}

void loop()                       // run over and over again
{

  Serial.print("Dit is een hele lange string zonder newline karakter");
  Serial.println(i);
  i++;
  delay(5000);
                                  // do nothing!
}
