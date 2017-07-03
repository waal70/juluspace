/**
 * 
 */
package nl.andredewaal.home.juluspace;

/**
 * @author awaal
 *
 */
final class Consts {
	public static final long TERM_SLEEP_INTERVAL = 500;
	public static final long TERM_MAX_SLEEP_MULTIPLIER = 10; // multiplier times sleep interval is the longest to wait after TERM
	
	/**
	 * Maximises the number of clips in the queue to play. Clips that already may have finished playing
	 * are also (still) in this queue, therefore, it is wise to periodically clean the list up.
	 * This number dictates the maximum number of entries in the list. Set it lower for systems
	 * that experience OutOfMemory errors.
	 */
	public static final int MAX_CLIP_BACKLOG = 255;
	
	public static final boolean FAKE = true;
	
	/////////SOUND FILES ARE HERE:
	//TODO: make the soundfiles platform independent, i.e., either take them from a configurable location, OR assume they are in ./soundfiles or something
	public static final String SND_I_AM_HAL = "/CIT.wav";
	public static final String SND_I_AM_HAL2 = "/CIT2.wav";
	public static final String SND_COMM_CHIRP_OPEN = "/commopen.wav" ;

}
