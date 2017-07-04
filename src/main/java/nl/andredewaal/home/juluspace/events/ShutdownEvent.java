/**
 * 
 */
package nl.andredewaal.home.juluspace.events;

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
