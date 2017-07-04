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

class ArduinoCommunicator implements SerialDataEventListener {

	private static Logger log = Logger.getLogger(ArduinoCommunicator.class);
	private Serial serialPort = null;

	private List<SpaceShipEvent> listeners = new ArrayList<SpaceShipEvent>();

	public void addListener(SpaceShipEvent addThis) {
		listeners.add(addThis);
	}

	public ArduinoCommunicator() {
		if (!Consts.FAKE) {
			serialPort = SerialFactory.createInstance();
			String osName = System.getProperty("os.name", "").toLowerCase();
			String defaultPort = "";
			// TODO: clean this up:
			if (osName.startsWith("windows")) {
				// windows
				defaultPort = "COM1";
			} else if (osName.startsWith("linux")) {
				// linux
				defaultPort = "/dev/ttyACM0";
			} else if (osName.startsWith("mac")) {
				// mac
				defaultPort = "????";
			} else {
				System.out.println("Sorry, your operating system is not supported");
				return;
			}

			SerialConfig config = new SerialConfig();

			// set default serial settings (device, baud rate, flow control, etc)
			//
			// by default, use the DEFAULT com port on the Raspberry Pi (exposed on GPIO
			// header)
			// NOTE: this utility method will determine the default serial port for the
			// detected platform and board/model. For all Raspberry Pi models
			// except the 3B, it will return "/dev/ttyAMA0". For Raspberry Pi
			// model 3B may return "/dev/ttyS0" or "/dev/ttyAMA0" depending on
			// environment configuration.
			// config.device("/dev/ttyACM0")
			config.device(defaultPort).baud(Baud._115200).dataBits(DataBits._8).parity(Parity.NONE)
					.stopBits(StopBits._1).flowControl(FlowControl.NONE);

			serialPort.addListener(this);
			log.debug("Added listener to port with config: " + config.toString());
			try {
				serialPort.open(config);
				serialPort.discardAll();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getLocalizedMessage());
			}
		} // Production mode
		else {
			log.debug("Added listener to port with config: FAKE setup");
		}

	}

	public void doThing() {
		String[] data = new String[8];
		data[0] = "EVENT 1";
		data[1] = "EVENT 2";
		data[2] = "EVENT 3 \n";
		data[3] = "EVENT 4";
		data[4] = "EVENT 5";
		data[5] = "EVENT 6";
		data[6] = "EVENT 7";
		data[7] = "EVENT 8";

		for (int i = 0; i < 8; i++) {
			dataReceived(data[i]);
		}
	}

	public void dataReceived(String event) {
		String receivedData = null;
		// IDEA for this method:
		// read a char (byte) from the serial bus. Full command reached when "\n"
		// received
		// This (the "\n") triggers the spaceShipEvent

		receivedData = event;
		log.info(receivedData);

		// notify own listeners:
		for (SpaceShipEvent sse : listeners)
			sse.spaceEvent(receivedData);
	}

	@Override
	public void dataReceived(SerialDataEvent event) {
		String receivedData = null;
		// IDEA for this method:
		// read a char (byte) from the serial bus. Full command reached when "\n"
		// received
		// This (the "\n") triggers the spaceShipEvent
		try {
			receivedData = event.getAsciiString();
			log.info(receivedData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// notify own listeners:
		for (SpaceShipEvent sse : listeners)
			sse.spaceEvent(receivedData);
	}

	public void writeSerial(String msg) {
		
		//use outputstream or writeln?
		if (serialPort != null)
		{
			try {
				serialPort.writeln(msg);
			} catch (IllegalStateException e) {
				log.debug("Cannot write to serial port.");
				log.error(e.getLocalizedMessage());
			} catch (IOException e) {
				log.debug("Cannot write to serial port");
				log.error(e.getLocalizedMessage());
			}
			
		}
	}

	public void stopListening() {
		if (!Consts.FAKE) {
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

		} else {
			log.debug("stopped listening");
		}
	}

}
