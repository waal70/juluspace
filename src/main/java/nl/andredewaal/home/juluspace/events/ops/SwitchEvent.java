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
public class SwitchEvent extends SpaceEvent {

	/**
	 * 
	 */
	public SwitchEvent(int switchOperated, String newValue) {
		this.type=SpaceEventType.SWITCH;
		this.index = switchOperated;
		this.payload = newValue;
		
	}

}
