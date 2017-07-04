/**
 * 
 */
package nl.andredewaal.home.juluspace.util;

import org.apache.log4j.Logger;

/**
 * @author awaal
 *
 */
public abstract class HIDInfo {
	private static Logger log = Logger.getLogger(HIDInfo.class);
	protected String soundName;
	protected int operationCount = 0;
	protected int newValue = 0;
	
	public HIDInfo(String soundName)
	{
		setSoundName(soundName);
	}
	
	public int getOperationCount() {
		return operationCount;
	}
	public int getNewValue()
	{
		return newValue;
	}
	public void reset()
	{
		log.debug("Resetting button presses");
		operationCount = 0;
	}
	public String getSoundName()
	{
		return soundName;
	}
	protected void setSoundName(String soundName) {
		this.soundName = soundName;
	}
	public void incOperationCount()
	{
		operationCount++;
	}


}
