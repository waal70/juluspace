package nl.andredewaal.home.juluspace;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import org.apache.log4j.Logger;

public class SpaceShipController implements SpaceShipEvent {
	private static Logger log = Logger.getLogger(SpaceShipController.class);

	private ArduinoListener al = null;
	private List<Clip> myclips = new ArrayList<Clip>();
	private boolean busy = true;
	private int globalCounter = 0;

	public SpaceShipController() {

		// Create an ArduinoListener and register self as listener
		al = new ArduinoListener();
		al.addListener(this);
		if (Consts.FAKE) al.doThing();
		// END FAKE WINDOWS
	}

	private void processSpaceEvent(String data) {

		log.debug("Processing, iteration: " + globalCounter);
		if (globalCounter == 2)
			kickOffSoundThread(Consts.SND_I_AM_HAL);
		else
			kickOffSoundThread(Consts.SND_COMM_CHIRP_OPEN);
		
		if (globalCounter ==2) new QuindarTone().intro();
		if (globalCounter ==3) new QuindarTone().outro();
		globalCounter++;

		if (globalCounter > 7)
			processTERMsignal();

	}

	@Override
	public void spaceEvent(String eventData) {
		// log.info("I received: " + eventData);
		processSpaceEvent(eventData);

	}

	private void processTERMsignal() {
		log.debug("TERM signal received, will wait a maximum of "+ Consts.TERM_MAX_SLEEP_MULTIPLIER * Consts.TERM_SLEEP_INTERVAL +" ms, now shutting down...");
		// al.stopListening();
		al.stopListening();
		log.debug("Told listener to stop receiving events");
		busy = SpaceShipSound.endSound(myclips);
		log.debug("Flag BUSY set to FALSE");
		myclips.clear();
		log.debug("Cleared the backlog of soundfiles");
		new QuindarTone().outro();
	}

	private void kickOffSoundThread(String soundName) {

		long randomSeed = (long) (Math.random() * 1000);
		log.debug("Randomly selected pre-load time for sound file of " + randomSeed + "ms.");
		try {
			Thread.sleep(randomSeed);
		} catch (InterruptedException e) {
			log.error(e.getLocalizedMessage());
			log.error("Regardless of failed sleep, still continuing");
		}

		//Check if cleanup needed, let's use a maximum of MAX_CLIP_BACKLOG
		if (myclips.size() > Consts.MAX_CLIP_BACKLOG)
			pruneMyClips();
		SpaceShipSound ps = new SpaceShipSound();
		//Only add the clip if the return is not null:
		Clip clipToAdd = ps.play(soundName, true, -10.0f);
		if (clipToAdd != null)
			myclips.add(clipToAdd);
		else
			log.debug("Ignored clip for: " + soundName);
		
	}
	
	/**
	 * This method will check whether all clips have been finished playing.
	 * If not, it will wait a certain amount of time, then, it will
	 * clear the myclips collection so that we may start out afresh...
	 */
	private void pruneMyClips()
	{
		log.debug("***********************Pruning. Size before: " + myclips.size());
		if (!SpaceShipSound.endSound(myclips))
			myclips.clear();
		log.debug("-----------------------Pruning finished. Size now: " + myclips.size());
	}

	public boolean isBusy() {
		return busy;
	}

}
