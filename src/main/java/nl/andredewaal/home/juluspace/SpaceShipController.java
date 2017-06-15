package nl.andredewaal.home.juluspace;

import org.apache.log4j.Logger;

public class SpaceShipController implements SpaceShipEvent {
	private static Logger log = Logger.getLogger(SpaceShipController.class);
	
	@SuppressWarnings("unused")
	private ArduinoListener al = null;
	private ArduinoListenerWIN alw = null;
	private boolean busy = true;
	
	private long globalCounter = 0;

	public SpaceShipController() {
		
		//Create an ArduinoListener and register self as listener
		//al = new ArduinoListener();
		//al.addListener(this);
		
		// to FAKE for WINDOWS:
		alw = new ArduinoListenerWIN();
		alw.addListener(this);
		alw.doThing();
		//END FAKE WINDOWS
	}

	private void processSpaceEvent(String data) {
		
		log.debug("Processing, iteration: " + globalCounter);
		kickOffSoundThread();
		globalCounter++;
		
		if (globalCounter > 3)
		{
			log.debug("Maximum iterations reached. Shutting down...");
			//al.stopListening();
			alw.stopListening();
			busy = false;
		}
		
	}
	
	@Override
	public void spaceEvent(String eventData) {
		//log.info("I received: " + eventData);
		processSpaceEvent(eventData);

	}
	private void kickOffSoundThread() {
		SpaceShipSound sss = new SpaceShipSound();
		Thread t = new Thread(sss);
		t.start();
		try {
			t.join(sss.getSleepTime());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			


	}
	
	public boolean isBusy()
	{
		return busy;
	}

}
