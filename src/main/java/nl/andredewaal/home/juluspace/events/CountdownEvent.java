/**
 * 
 */
package nl.andredewaal.home.juluspace.events;

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
