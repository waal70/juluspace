#include <avr/wdt.h>
long i;

void setup()                    // run once, when the sketch starts
{
 
  Serial.begin(115200);           // set up Serial library at 115200 bps

  Serial.println("BEGIN");  // prints hello with ending line break
  i = 100;
  //Messages over the serial bus have the following format:
  //Xnnn:nnn, so for example: B100:1, meaning button 100 has been turned on.
 // wdt_enable(WDTO_8S); //This has to be the last instruction in setup()
}

void loop()                       // run over and over again
{

  Serial.print("B");
  Serial.print(i);
  Serial.println(":00");
  i++;
  //wdt_delay(2000);
  delay(2000);
  Serial.println("S80:0");
  if (i == 103)
  {
    Serial.println("LAUNCH");
  }
  if (i == 108)
  {
    Serial.println("TERM");
  }
  if (i == 110)
  {
    //try and trigger a reset:
    //wdt_reload();
    software_Reset();
  }
  // do nothing!
  //wdt_delay(5000); //Last statement in loop()
}

void wdt_delay(unsigned long msec) {
  wdt_reset();

  while (msec > 1000) {
    wdt_reset();
    delay(1000);
    msec -= 1000;
  }
  delay(msec);
  wdt_reset();
}
void wdt_reload() {
  Serial.end();
  delay(9000);
}

void software_Reset()
// Restarts program from beginning but 
// does not reset the peripherals and registers
{
  Serial.flush();
  Serial.end();
  asm volatile ("  jmp 0");  
} 
