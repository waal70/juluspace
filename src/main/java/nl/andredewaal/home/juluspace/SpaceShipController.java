package nl.andredewaal.home.juluspace;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import org.apache.log4j.Logger;

public class SpaceShipController implements SpaceShipEvent {
	private static Logger log = Logger.getLogger(SpaceShipController.class);

	@SuppressWarnings("unused")
	private ArduinoListener al = null;
	private ArduinoListenerWIN alw = null;
	private List<Clip> myclips = new ArrayList<Clip>();
	private boolean busy = true;
	private int globalCounter = 0;

	public SpaceShipController() {

		// Create an ArduinoListener and register self as listener
		// al = new ArduinoListener();
		// al.addListener(this);

		// to FAKE for WINDOWS:
		alw = new ArduinoListenerWIN();
		alw.addListener(this);
		alw.doThing();
		// END FAKE WINDOWS
	}

	private void processSpaceEvent(String data) {

		log.debug("Processing, iteration: " + globalCounter);
		if (globalCounter == 2)
			kickOffSoundThread(Consts.SND_I_AM_HAL);
		else
			kickOffSoundThread(Consts.SND_COMM_CHIRP_OPEN);
		
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
		alw.stopListening();
		log.debug("Told listener to stop receiving events");
		busy = SpaceShipSound.endSound(myclips);
		log.debug("Flag BUSY set to FALSE");
		myclips.clear();
		log.debug("Cleared the backlog of soundfiles");
	}

	private void kickOffSoundThread(String soundName) {

		long randomSeed = (long) (Math.random() * 1000);
		log.debug("Randomly selected pre-load time for sound file of " + randomSeed + "ms.");
		try {
			Thread.sleep(randomSeed);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SpaceShipSound ps = new SpaceShipSound();
		try {
			myclips.add(ps.play(soundName, true, -10.0f));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean isBusy() {
		return busy;
	}

}
