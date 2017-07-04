/**
 * 
 */
package nl.andredewaal.home.juluspace.events.complex;

import nl.andredewaal.home.juluspace.events.SpaceEvent;
import nl.andredewaal.home.juluspace.events.SpaceEventType;
import nl.andredewaal.home.juluspace.util.Consts;
/**
 * @author awaal
 *
 */
public class SoundEvent extends SpaceEvent {
	
	
	public SoundEvent(String soundName)
	{
		this.payload = soundName;
		this.type = SpaceEventType.SOUND;
	}
	public SoundEvent() {
		this(Consts.SND_COMM_CHIRP_OPEN);
		
	}
	
}
