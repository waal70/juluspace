/**
 * 
 */
package nl.andredewaal.home.juluspace.events.complex;

import nl.andredewaal.home.juluspace.events.SpaceEvent;
import nl.andredewaal.home.juluspace.events.SpaceEventType;

/**
 * @author awaal
 *
 */
public class CountdownEvent extends SpaceEvent {
	
	public CountdownEvent()
	{
		this.type= SpaceEventType.COUNTDOWN;
	}

}
