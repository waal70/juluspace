/**
 * 
 */
package nl.andredewaal.home.juluspace.util;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * @author awaal
 * This class will contain the mapping for the spaceship
 * i.e., when the serial port sends information on a button
 * press, this will be able to convert into the proper sequence
 */
public class ShipStatusMapper {

	private static Logger log = Logger.getLogger(ShipStatusMapper.class);
	private HashMap<Integer, ButtonInfo> buttonMap = new HashMap<Integer, ButtonInfo>(); 
	private HashMap<Integer, SwitchInfo> switchMap = new HashMap<Integer, SwitchInfo>();
	private HashMap<Integer, RotaryEncoderInfo> rotaryEncoderMap = new HashMap<Integer, RotaryEncoderInfo>();

	/**
	 * 
	 */
	public ShipStatusMapper() {
		populateButtons();
		populateSwitches();
	}
	
	public String getButton(int buttonNumber)
	{
		ButtonInfo button = buttonMap.get(buttonNumber);
		String temp = null;
		if (button != null)
			temp = buttonMap.get(buttonNumber).getSoundName();
		if (temp == null)
		{
			log.warn("Non-mapped button referenced: " + buttonNumber);
			temp = Consts.SND_COMM_CHIRP_OPEN;
		}
		return temp;
	}
	
	public HashMap<Integer, ButtonInfo> getButtonMap()
	{
		return buttonMap;
	}
	
	public int getSwitchValue(int switchNumber)
	{
		SwitchInfo si = switchMap.get(switchNumber);
		int returnValue = 0;
		if (si != null)
			returnValue = switchMap.get(switchNumber).getNewValue();
		else
			log.warn("Non-mapped switch referenced: " + switchNumber);
		return returnValue;
	}
	public int getRotaryEncoderValue(int rotaryEncoderNumber)
	{
		RotaryEncoderInfo rei = rotaryEncoderMap.get(rotaryEncoderNumber);
		int returnValue = 0;
		if (rei != null)
			returnValue = rotaryEncoderMap.get(rotaryEncoderNumber).getNewValue();
		else
			log.warn("Non-mapped rotary encoder referenced: " + rotaryEncoderNumber);
		return returnValue;
	}
	
	public void incrementButtonPress(int buttonNumber)
	{
		if (buttonMap.get(buttonNumber) != null)
		{
			log.debug("Times pressed before: " + buttonMap.get(buttonNumber).getTimesPressed());
			buttonMap.get(buttonNumber).incTimesPressed();
			log.debug("Times pressed after: " + buttonMap.get(buttonNumber).getTimesPressed());
		}
		else
			log.warn("Non-mapped button referenced: " + buttonNumber);
		
	}
	public void incrementSwitchUsage(int switchNumber)
	{
		if (switchMap.get(switchNumber) != null)
		{
			log.debug("Times operated before: " + switchMap.get(switchNumber).getOperationCount());
			switchMap.get(switchNumber).incOperationCount();
			log.debug("Times operated after: " + switchMap.get(switchNumber).getOperationCount());
		}
		else
			log.warn("Non-mapped switch referenced: " + switchNumber);
	}

	private void populateButtons() {
		buttonMap.put(100, new ButtonInfo(Consts.SND_COMM_CHIRP_OPEN));
		buttonMap.put(101, new ButtonInfo(Consts.SND_I_AM_HAL));
		
	}
	private void populateSwitches()
	{
		switchMap.put(80, new SwitchInfo(Consts.SND_COMM_CHIRP_OPEN, 0));
	}

}
