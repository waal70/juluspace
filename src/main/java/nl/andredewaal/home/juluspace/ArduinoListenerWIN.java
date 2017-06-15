package nl.andredewaal.home.juluspace;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


class ArduinoListenerWIN {
	
	private static Logger log = Logger.getLogger(ArduinoListenerWIN.class);


	private List<SpaceShipEvent> listeners = new ArrayList<SpaceShipEvent>();
	
	public void addListener(SpaceShipEvent addThis) {
		listeners.add(addThis);
	}
	public void doThing()
	{
		String[] data = new String[4];
		data[0]="EVENT 1";
		data[1]="EVENT 2";
		data[2]="EVENT 3 \n";
		data[3]="EVENT 4";
				
		for (int i = 0;i<4;i++)
		{
			dataReceived(data[i]);
		}
	}
	
	public ArduinoListenerWIN()
	{
		    log.debug("Added listener to port with config: WINDOWS");

	}
		

    public void dataReceived(String event)
    {
    	String receivedData = null;
    	//IDEA for this method:
    	// read a char (byte) from the serial bus. Full command reached when "\n" received
    	// This (the "\n") triggers the spaceShipEvent
    	
    		receivedData = event;
			log.info(receivedData);
		
    	//notify own listeners:
		 for (SpaceShipEvent sse : listeners)
		        sse.spaceEvent(receivedData);
        }
    
    
    public void stopListening()
    {
    	log.debug("stopped listening");
    }



}
