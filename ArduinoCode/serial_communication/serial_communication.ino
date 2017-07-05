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
  i = 100;
  //Messages over the serial bus have the following format:
  //Xnnn:nnn, so for example: B100:1, meaning button 100 has been turned on.
  
}

void loop()                       // run over and over again
{

  Serial.print("B");
  Serial.print(i);
  Serial.println(":00");
  i++;
  delay(5000);
  Serial.println("S80:0");
  if (i==103)
  {
    Serial.println("LAUNCH");
  }
  if (i==108)
  {
    Serial.println("TERM");
  }
                                  // do nothing!
}
