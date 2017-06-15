package nl.andredewaal.home.juluspace;

import org.apache.log4j.Logger;

public class SpaceShipController implements SpaceShipEvent {
	private static Logger log = Logger.getLogger(SpaceShipController.class);
	
	private ArduinoListener al = null;
	private boolean busy = true;
	
	private long globalCounter = 0;

	public SpaceShipController() {
		
		//Create an ArduinoListener and register self as listener
		al = new ArduinoListener();
		al.addListener(this);

	}

	private void processSpaceEvent(String data) {
		
		log.debug("Processing, iteration: " + globalCounter);
		globalCounter++;
		
		if (globalCounter > 3)
		{
			log.debug("Maximum iterations reached. Shutting down...");
			al.stopListening();
			busy = false;
		}
		
	}
	
	@Override
	public void spaceEvent(String eventData) {
		//log.info("I received: " + eventData);
		processSpaceEvent(eventData);

	}
	
	public boolean isBusy()
	{
		return busy;
	}

}
