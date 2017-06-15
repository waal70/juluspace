/**
 * 
 */
package nl.andredewaal.home.juluspace;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;

/**
 * @author awaal
 *
 */
public class SpaceShipSound implements Runnable {
	private static Logger log = Logger.getLogger(SpaceShipSound.class);
	private String soundFileName = "E:\\CODE\\EclipseWorkspaces\\juluspace\\src\\main\\resources\\commopen.wav";
	private int numberIterations = 1;
	private long playTime = 1000;
	/**
	 * 
	 */
	public SpaceShipSound() {
		// TODO Auto-generated constructor stub
	}
	
	public long getSleepTime()
	{
		return playTime * numberIterations;
	}

	public SpaceShipSound(String fileToPlay) {
		soundFileName = fileToPlay;
	}

	public SpaceShipSound(String fileToPlay, int repetitions) {
		soundFileName = fileToPlay;
		numberIterations = repetitions;
	}

	@Override
	public void run() {
		log.debug("Sound Thread started.");
		log.debug("Instructed to do " + numberIterations + " iterations.");

		for (int i = 0; i < numberIterations; i++) {
			playSound();
		}
		log.debug("Completed");
	}

	public void playSound() {
		Mixer.Info target = null;
		for (Mixer.Info info : AudioSystem.getMixerInfo()) {
			log.info(info.getName());
			if (info.getName().startsWith("ALSA"))
				target = info;
		}
		Mixer mm = AudioSystem.getMixer(target); // gets the default mixer

		Mixer.Info inf = mm.getMixerInfo();
		log.info("default mixer is: " + inf.getName());
		Line.Info lin = mm.getLineInfo();
		log.info("not specified is: " + AudioSystem.NOT_SPECIFIED);
		log.info("Max lines for default mixer: " + mm.getMaxLines(lin));

		File yourFile;
		AudioInputStream stream = null;
		AudioFormat format;
		DataLine.Info info;
		Clip clip = null;
		yourFile = new File(soundFileName);
		try {
			stream = AudioSystem.getAudioInputStream(yourFile);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volumeControl.setValue(-40.0f); // Reduce volume by 10 decibels.
			clip.start();
			playTime = clip.getMicrosecondLength();
			Thread.sleep(playTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e.getLocalizedMessage());
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			log.error(e.getLocalizedMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getLocalizedMessage());
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			log.error(e.getLocalizedMessage());
		}

	}
}
