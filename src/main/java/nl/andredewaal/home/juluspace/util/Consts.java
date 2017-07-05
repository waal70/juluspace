/**
 * 
 */
package nl.andredewaal.home.juluspace.util;

/**
 * @author awaal
 *
 */
public final class Consts {
	public static final long TERM_SLEEP_INTERVAL = 500;
	public static final long TERM_MAX_SLEEP_MULTIPLIER = 50; // multiplier times sleep interval is the longest to wait after TERM
	/**
	 * Defines the duration of waiting milliseconds before starting countdown
	 */
	public static final long LAUNCH_WAIT = 12000;
	
	/**
	 * Maximises the number of clips in the queue to play. Clips that already may have finished playing
	 * are also (still) in this queue, therefore, it is wise to periodically clean the list up.
	 * This number dictates the maximum number of entries in the list. Set it lower for systems
	 * that experience OutOfMemory errors.
	 */
	public static final int MAX_CLIP_BACKLOG = 255;
	public static final int OFF = 0;
	public static final int ON = 1;
	
	public static final boolean FAKE = false;
	
	/////////SOUND FILES ARE HERE:
	//TODO: make the soundfiles platform independent, i.e., either take them from a configurable location, OR assume they are in ./soundfiles or something
	public static final String SND_I_AM_HAL = "/CIT.wav";
	public static final String SND_I_AM_HAL2 = "/CIT2.wav";
	public static final String SND_COMM_CHIRP_OPEN = "/commopen.wav" ;
	public static final String SND_LAUNCH = "/Launch-Sound_Soyuz-Launch.wav";
	public static final String SND_COUNTDOWN = "/Launch-Sound_Female-Voice-Countdown.wav";
	public static final String SND_SHUTDOWN = "/640174main_Wheel Stop.wav";
	public static final String SND_R2D2 ="/R2D2-extra.wav";

}
