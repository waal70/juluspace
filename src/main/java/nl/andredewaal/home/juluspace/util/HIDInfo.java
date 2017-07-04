/**
 * 
 */
package nl.andredewaal.home.juluspace.util;

/**
 * @author awaal
 *
 */
public abstract class HIDInfo {
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
