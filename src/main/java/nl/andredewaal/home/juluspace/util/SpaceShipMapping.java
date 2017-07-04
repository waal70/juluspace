/**
 * 
 */
package nl.andredewaal.home.juluspace.util;

import java.util.HashMap;

/**
 * @author awaal
 * This class will contain the mapping for the spaceship
 * i.e., when the serial port sends information on a button
 * press, this will be able to convert into the proper sequence
 */
public class SpaceShipMapping {
	
	private HashMap<Integer, String> buttonMap = new HashMap<Integer, String>(); 

	/**
	 * 
	 */
	public SpaceShipMapping() {
		populateButtons();
	}
	
	public String getButton(int buttonNumber)
	{
		String temp = buttonMap.get(buttonNumber);
		if (temp == null)
			temp = Consts.SND_COMM_CHIRP_OPEN;
		return temp;
	}

	private void populateButtons() {
		buttonMap.put(100, Consts.SND_COMM_CHIRP_OPEN);
		buttonMap.put(101, Consts.SND_I_AM_HAL);
		
	}

}
