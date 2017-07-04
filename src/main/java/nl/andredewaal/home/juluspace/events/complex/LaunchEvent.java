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
public class LaunchEvent extends SpaceEvent {
	
	public LaunchEvent()
	{
		this.type=SpaceEventType.LAUNCH;
	}

}
