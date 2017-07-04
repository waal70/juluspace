/**
 * 
 */
package nl.andredewaal.home.juluspace.util;

/**
 * @author awaal
 *
 */
public class ButtonInfo extends HIDInfo {
	
	public ButtonInfo(String soundName) {
		super(soundName);
	}
	public int getTimesPressed() {
		return getOperationCount();
	}
	public void incTimesPressed()
	{
		incOperationCount();
	}
}
