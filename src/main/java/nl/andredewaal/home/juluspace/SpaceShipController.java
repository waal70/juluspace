package nl.andredewaal.home.juluspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;

import javax.sound.sampled.Clip;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.andredewaal.home.juluspace.events.SpaceEvent;
import nl.andredewaal.home.juluspace.events.complex.SoundEvent;
import nl.andredewaal.home.juluspace.tasks.LaunchTask;
import nl.andredewaal.home.juluspace.tasks.SurveyInterfaceTask;
import nl.andredewaal.home.juluspace.util.ButtonInfo;
import nl.andredewaal.home.juluspace.util.Consts;
import nl.andredewaal.home.juluspace.util.ShipStatusMapper;

/**
 * @author awaal
 * This class is the main "brain" of the SpaceShip. It determines courses of action,
 * sounds to play, and manages spaceship startup and shutdown.
 */
public class SpaceShipController {
	private static Logger log = LogManager.getLogger(SpaceShipController.class);

	private ShipStatusMapper ssm = new ShipStatusMapper();
	/**
	 * Periodically inspects the state of buttons, switches, encoders, etc.
	 */
	private Timer launchTimer = new Timer("LaunchTimer");
	private Timer surveyTimer = new Timer("SurveyTimer");
	/**
	 * The timer to sequence events for the Apollo 13 mishap.
	 * "Houston, we've had a problem"
	 */
	//private Timer apollo13Timer = new Timer("Apollo13");
	
	/**
	 * Sequence the events for a simulated lightning strike
	 */
	//private Timer lightningStrikeTimer = new Timer("LightningStrike"); 

	private ArduinoCommunicator ac = null;
	private List<Clip> myclips = new ArrayList<Clip>();
	private boolean busy = true;
	private boolean launching = false;
	private boolean shutdownPending = false;

	public SpaceShipController() {

		// Create an ArduinoCommunicator and register self as listener
		ac = new ArduinoCommunicator();
		ac.addListener(this);
		//Timer surveyTimer = new Timer("SurveyTimer");
		surveyTimer.schedule(new SurveyInterfaceTask(this), 1000, 4000);
		if (Consts.FAKE)
			ac.doThing();
		// END FAKE WINDOWS
	}

	private void processLaunch() {
		if (launching) {
			log.debug("Launch already ongoing, ignoring new request");
		} else {
			log.info("Processing Launch Sequence....");
			launchTimer.schedule(new LaunchTask(this), Consts.LAUNCH_WAIT);
			kickOffSoundThread(Consts.SND_LAUNCH, Consts.VOL_REDUCED);
		}
	}

	public void spaceEvent(SpaceEvent event) {
		if (!shutdownPending) {
		switch (event.type) {
		case BUTTON:
			processButtonPress(event.index);
			break;
		case SWITCH:
			processSwitchChange(event.index);
			break;
		case LAUNCH:
			processLaunch();
			break;
		case SOUND:
			kickOffSoundThread(event.payload);
			break;
		case SHUTDOWN:
			processTERMsignal();
			break;
		case COUNTDOWN:
			kickOffSoundThread(Consts.SND_COUNTDOWN, Consts.VOL_LOUD);
			break;
		default:
			kickOffSoundThread(Consts.SND_COMM_CHIRP_OPEN);
			break;
		}
		 }
		 else
		 log.info("Ignoring event because shutdown pending");

	}

	public void surveyInterface()
	{
		log.debug("Surveying buttons...");
		//loop over all buttons. If one of them is pressed more than 3 times, fire event.
		HashMap<Integer, ButtonInfo> buttonMap = ssm.getButtonMap();
		Iterator<Entry<Integer, ButtonInfo>> it = buttonMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, ButtonInfo> pair = it.next();
			if (pair.getValue().getTimesPressed() > 2)
			{
				log.debug("Found button that was pressed more than twice.");
				pair.getValue().reset();
				spaceEvent(new SoundEvent(Consts.SND_R2D2));
			}
		}
	}
	private void processSwitchChange(int index) {
		ssm.incrementSwitchUsage(index);
		log.debug("========================SWITCH OPERATED, new value = " + ssm.getSwitchValue(index));
		
	}

	private void processButtonPress(int index) {
		ssm.incrementButtonPress(index);
		kickOffSoundThread(ssm.getButton(index));
	}

	private void processTERMsignal() {
		log.info("TERM signal received");
		shutdownPending = true;
		//cancel all timers, otherwise, there will never be an exit:
		surveyTimer.cancel();
		launchTimer.cancel();
		log.debug("TERM signal received, will wait a maximum of "
				+ Consts.TERM_MAX_SLEEP_MULTIPLIER * Consts.TERM_SLEEP_INTERVAL + " ms, now shutting down...");
		ac.stopListening();
		log.debug("Told listener to stop receiving events");
		boolean soundStop = SpaceShipSound.endSound(myclips);
		// Give audible feedback to the user, using the modal setting
		kickOffSoundThread(Consts.SND_SHUTDOWN, Consts.VOL_DEFAULT, true);
		myclips.clear();
		log.debug("Cleared the backlog of soundfiles");
		new QuindarTone().outro();
		busy = soundStop;
		log.debug("Busy FLAG set to " + busy);
	}

	/**
	 * @param soundName
	 *            The name of the sound file to play Method will default to a gain
	 *            of -10.0f and non-modal sound. This means processing will continue
	 *            and other sound files may be stacked on top of this sound
	 */
	private void kickOffSoundThread(String soundName) {
		kickOffSoundThread(soundName, Consts.VOL_DEFAULT, false);
	}

	/**
	 * @param soundName
	 *            The name of the sound file to play
	 * @param gain
	 *            The volume in the mixer. Negative numbers only, please Method will
	 *            default to a non-modal sound, meaning processing will continue,
	 *            possibly stacking other sound files, while this sound is playing.
	 */
	private void kickOffSoundThread(String soundName, float gain) {
		kickOffSoundThread(soundName, gain, false);
	}

	/**
	 * @param soundName
	 *            The name of the sound file to play
	 * @param gain
	 *            The volume in the mixer. Negative numbers only, please.
	 * @param modal
	 *            True when the entire processing should yield to this sound
	 */
	private void kickOffSoundThread(String soundName, float gain, boolean modal) {

		// Check if cleanup needed, let's use a maximum of MAX_CLIP_BACKLOG
		if (myclips.size() > Consts.MAX_CLIP_BACKLOG)
			pruneMyClips();
		SpaceShipSound ps = new SpaceShipSound();
		// Only add the clip if the return is not null:
		Clip clipToAdd = ps.play(soundName, true, gain, modal);
		if (clipToAdd != null)
			myclips.add(clipToAdd);
		else
			log.debug("Ignored clip for: " + soundName);

	}

	/**
	 * This method will check whether all clips have been finished playing. If not,
	 * it will wait a certain amount of time, then, it will clear the myclips
	 * collection so that we may start out afresh...
	 */
	private void pruneMyClips() {
		log.debug("***********************Pruning. Size before: " + myclips.size());
		if (!SpaceShipSound.endSound(myclips))
			myclips.clear();
		log.debug("-----------------------Pruning finished. Size now: " + myclips.size());
	}

	public boolean isBusy() {
		return busy;
	}

}
