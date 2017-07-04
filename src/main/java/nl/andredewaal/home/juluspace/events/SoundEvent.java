/**
 * 
 */
package nl.andredewaal.home.juluspace.events;

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
