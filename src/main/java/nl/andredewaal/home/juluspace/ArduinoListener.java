package nl.andredewaal.home.juluspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;


class ArduinoListener implements SerialDataEventListener {
	
	private static Logger log = Logger.getLogger(ArduinoListener.class);
	private final Serial serialPort = SerialFactory.createInstance();

	private List<SpaceShipEvent> listeners = new ArrayList<SpaceShipEvent>();
	
	public void addListener(SpaceShipEvent addThis) {
		listeners.add(addThis);
	}
	
	public ArduinoListener()
	{
		SerialConfig config = new SerialConfig();

        // set default serial settings (device, baud rate, flow control, etc)
        //
        // by default, use the DEFAULT com port on the Raspberry Pi (exposed on GPIO header)
        // NOTE: this utility method will determine the default serial port for the
        //       detected platform and board/model.  For all Raspberry Pi models
        //       except the 3B, it will return "/dev/ttyAMA0".  For Raspberry Pi
        //       model 3B may return "/dev/ttyS0" or "/dev/ttyAMA0" depending on
        //       environment configuration.
        config.device("/dev/ttyACM0")
              .baud(Baud._115200)
              .dataBits(DataBits._8)
              .parity(Parity.NONE)
              .stopBits(StopBits._1)
              .flowControl(FlowControl.NONE);

        	serialPort.addListener(this);
		    log.debug("Added listener to port with config: " + config.toString());
		    try {
				serialPort.open(config);
				serialPort.discardAll();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getLocalizedMessage());
			}
		

	}
		
    @Override
    public void dataReceived(SerialDataEvent event)
    {
    	String receivedData = null;
    	//IDEA for this method:
    	// read a char (byte) from the serial bus. Full command reached when "\n" received
    	// This (the "\n") triggers the spaceShipEvent
    	try {
    		receivedData = event.getAsciiString();
			log.info(receivedData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//notify own listeners:
		 for (SpaceShipEvent sse : listeners)
		        sse.spaceEvent(receivedData);
        }
    
    
    public void stopListening()
    {
    	try {
    		log.debug("De-registering listener");
			serialPort.removeListener(this);
			serialPort.discardAll();
	    	log.debug("Closing Port");
	    	serialPort.close();
		
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			log.error(e.getLocalizedMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getLocalizedMessage());
		}

    }



}
