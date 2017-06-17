package nl.andredewaal.home.juluspace;

import java.io.File;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.FloatControl;

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

	    public Clip play(String filename, boolean autostart, float gain) throws Exception {
	    	File yourfile = new File(filename);
	    	AudioInputStream stream = AudioSystem.getAudioInputStream(yourfile);
			AudioFormat format = stream.getFormat();
	        
			if (stream.getFormat().getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
	            stream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, stream);
	        }
			Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volumeControl.setValue(-40.0f); // Reduce volume by 10 decibels.
			log.debug("Starting clip " + filename);
	        if (autostart) clip.start();
	        //log.debug("setting clip");
	        return clip;
	    }
}
