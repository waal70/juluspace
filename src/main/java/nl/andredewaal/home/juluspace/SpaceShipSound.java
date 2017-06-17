package nl.andredewaal.home.juluspace;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;

public class SpaceShipSound {
	private static Logger log = Logger.getLogger(SpaceShipSound.class);
	
	public static boolean endSound(List<Clip> allClips) //Consider this list the backlog of all soundfiles.
		{
			// loop through all clips and see if one is still running:
		log.debug("Looping through all active sounds...");
			int multiplier = 0;
			for (Clip currentClip : allClips)
				while (currentClip.isRunning()) {
					multiplier++;
					if (multiplier > Consts.TERM_MAX_SLEEP_MULTIPLIER) {
						log.debug("Playing will take too long. Forcefully stopping.");
						break;
					}
					log.debug("Waiting for clip: (current/total) ms:(" + currentClip.getMicrosecondPosition() / 1000 + "/"
							+ currentClip.getMicrosecondLength() / 1000 + ") ms");
					try {
						Thread.sleep(Consts.TERM_SLEEP_INTERVAL);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			return false;
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
	    public Clip play(String soundName, boolean autostart, float gain) {
	    	
	    	if (gain == 0) gain=-40.0f;
	       	File yourfile = null;
			try {
				yourfile = new File(SpaceShipSound.class.getResource(soundName).toURI());
			} catch (URISyntaxException e) {
				log.error(e.getLocalizedMessage());
			}
	    	AudioInputStream stream =null;
			try {
				stream = AudioSystem.getAudioInputStream(yourfile);
			} catch (UnsupportedAudioFileException | IOException e) {
				log.error(e.getLocalizedMessage());
			}
			AudioFormat format = stream.getFormat();
	        
			if (stream.getFormat().getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
	            stream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, stream);
	        }
			Info info = new DataLine.Info(Clip.class, format);
			Clip clip = null;
			try {
				clip = (Clip) AudioSystem.getLine(info);
			} catch (LineUnavailableException e) {
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
	        return clip;
	    }
}
