/**
 * 
 */
package nl.andredewaal.home.juluspace.events.ops;

import nl.andredewaal.home.juluspace.events.SpaceEvent;
import nl.andredewaal.home.juluspace.events.SpaceEventType;

/**
 * @author awaal
 *
 */
public class ButtonPressEvent extends SpaceEvent {
	
	/**
	 * 
	 */
	public ButtonPressEvent(int buttonNumber)
	{
		this.type=SpaceEventType.BUTTON;
		this.index = buttonNumber;
	}

}
