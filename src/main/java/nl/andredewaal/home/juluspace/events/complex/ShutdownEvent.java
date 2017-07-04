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
public class ShutdownEvent extends SpaceEvent {
	
	public ShutdownEvent()
	{
		this.type = SpaceEventType.SHUTDOWN;
	}

}
