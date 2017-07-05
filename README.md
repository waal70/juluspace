# spaceship
Arduino / Raspberry PI project to control a toy spaceship

Plans for the Arduino implementation:
* Maintain a map of all button states
* Maintain a map of all switch states
* Maintain a map of all rotary encoder states
Assume init state for all is off, 0, unswitched

ONLY write to the serial bus if, upon scan, the unit appears to be in a different state

For rotary encoders, maintain a small bandwith (e.g.) 10 -> only write when the value differs more than 10

Things to think about:
Master Caution button should have a dedicated interface? - I think not, just remember the button number :)
Have the Arduino handle some things independently, such as "TERM"?
Have the Arduino handle some things independently, such as the ligthing of the PTT-button?

