package nl.andredewaal.home.juluspace;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;

import nl.andredewaal.home.juluspace.util.Consts;

/**
 * @author awaal
 *
 * This class handles the playing of sound files, primarily on the Raspberry PI, but
 * it will also handle Windows Systems.
 */
public class SpaceShipSound {
	private static Logger log = Logger.getLogger(SpaceShipSound.class);
	private Clip clip = null;
	
	/**
	 * @param allClips
	 * The method will loop through all clips that were loaded for play and checks whether
	 * any of them are still playing.
	 * If they are, the method sleeps for TERM_SLEEP_INTERVAL and checks again.
	 * If the amount estimated to wait is bigger then configured, the method will forcefully stop
	 * @return false when all sounds have been completed 
	 */
	public static boolean endSound(List<Clip> allClips) //Consider this list the backlog of all soundfiles.
		{
			// loop through all clips and see if one is still running:
		log.debug("Looping through all active sounds...");
			int multiplier = 0;
			for (Iterator<Clip> iterator = allClips.iterator(); iterator.hasNext();) {
				Clip currentClip = iterator.next();
				while (currentClip.isRunning()) {
					multiplier++;
					if (multiplier > Consts.TERM_MAX_SLEEP_MULTIPLIER) {
						log.debug("Playing will take too long. Forcefully stopping.");
						iterator = null;
						closeAll(allClips);
						return false;
					}
					log.debug("Waiting for clip: (current/total) ms:(" + currentClip.getMicrosecondPosition() / 1000 + "/"
							+ currentClip.getMicrosecondLength() / 1000 + ") ms");
					waitSleep(Consts.TERM_SLEEP_INTERVAL);
				}
			}
			return false;
		}
	private static void closeAll(List<Clip> allClips)
	{
		for (Iterator<Clip> iterator = allClips.iterator(); iterator.hasNext();) 
		{
			Clip currentClip = iterator.next();
			if (currentClip.isOpen()) currentClip.stop();
		}
	}
	public Clip play(String soundName, boolean autostart, float gain)
	{
		return play(soundName, autostart, gain, false);
	}

	    /**
	     * @param soundName The resource that contains the filename to be played. These files
	     * are found in resources and it should be a WAV-file
	     * @param autostart Indicates whether to start playing the file automatically. If not,
	     * callers should call the start() method on the Clip itself
	     * @param gain The volume for the soundclip
	     * @return The Clip instance with the settings pre-configured
	     * @throws Exception
	     */
	    public Clip play(String soundName, boolean autostart, float gain, boolean modal) {

	    	if (gain == 0) gain=-40.0f;
	    	log.debug("Gain is set to: " + gain);
	       	InputStream dumbInputstream = null;
			dumbInputstream = getClass().getResourceAsStream(soundName);
			InputStream yourfile = new BufferedInputStream(dumbInputstream);
		   	AudioInputStream stream =null;
			try {
				stream = AudioSystem.getAudioInputStream(yourfile);
			} catch (UnsupportedAudioFileException | IOException e) {
				log.error(e.getLocalizedMessage());
				return null;
			}
			//if (stream == null) return null;
			
			AudioFormat format = stream.getFormat();
			
			if (format == null) return null;
	        
			if (stream.getFormat().getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
	            stream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, stream);
	        }
			Info info = new DataLine.Info(Clip.class, format);
			try {
				clip = (Clip) AudioSystem.getLine(info);
				//Added this to resolve line availability issues on Arduino:
				clip.addLineListener(event -> 
				{
				    if(LineEvent.Type.STOP.equals(event.getType())) {
				    	log.debug("Closing clip because STOP event received");
				    	clip.close();
				    	clip.removeLineListener((LineListener) this);
				    	//event.getLine().removeLineListener(event ->);
			    }
				});
			} catch (LineUnavailableException | IllegalArgumentException e) {
				log.error(e.getLocalizedMessage());
			}
			try {
				clip.open(stream);
			} catch (LineUnavailableException | IOException e) {
				log.error(e.getLocalizedMessage());
			}
			FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volumeControl.setValue(gain); // .
			log.debug("Starting clip " + soundName);
	        if (autostart) clip.start();
	        if (modal)
	        {
	        	log.debug("Modal clip wait sequence START");
	        	log.debug(clip.isRunning());
	        	while (clip.isRunning() || clip.isOpen())
	        	{
	        		waitSleep(Consts.TERM_SLEEP_INTERVAL);
					log.debug("In modal clip wait sequence...");
	        	}
	        	log.debug("Modal clip wait sequence END");
	        }
	        return clip;
	    }
	    private static void waitSleep(long millis)
	    {
	    	try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				log.error("Unable to sleep thread: " + e.getLocalizedMessage());
				//waitSleep(millis);
				}
	    	
	    }
}
