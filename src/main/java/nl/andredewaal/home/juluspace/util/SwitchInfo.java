/**
 * 
 */
package nl.andredewaal.home.juluspace.util;

/**
 * @author awaal
 *
 */
public class SwitchInfo extends HIDInfo {

	/**
	 * @param soundName
	 */
	public SwitchInfo(String soundName, int newValue) {
		super(soundName);
		this.newValue = newValue;
	}

}
