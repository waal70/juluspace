package nl.andredewaal.home.juluspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

import nl.andredewaal.home.juluspace.events.SpaceEvent;
import nl.andredewaal.home.juluspace.events.complex.LaunchEvent;
import nl.andredewaal.home.juluspace.events.complex.ShutdownEvent;
import nl.andredewaal.home.juluspace.events.ops.ButtonPressEvent;
import nl.andredewaal.home.juluspace.events.ops.MasterCautionPressedEvent;
import nl.andredewaal.home.juluspace.events.ops.RotaryEncoderEvent;
import nl.andredewaal.home.juluspace.events.ops.SwitchEvent;
import nl.andredewaal.home.juluspace.util.Consts;

class ArduinoCommunicator implements SerialDataEventListener {

	private static Logger log = LogManager.getLogger(ArduinoCommunicator.class);
	private Serial serialPort = null;

	private List<SpaceShipController> listeners = new ArrayList<SpaceShipController>();

	public synchronized void addListener(SpaceShipController spaceShipController) {
		listeners.add(spaceShipController);
	}
	public synchronized void removeListener(SpaceShipController removeThis) {
		listeners.remove(removeThis);
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
				log.info("Sorry, your operating system is not supported");
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
			log.info("Added listener to port with config: " + config.toString());
			try {
				serialPort.open(config);
				serialPort.discardAll();
			} catch (IOException e) {
				log.error(e.getLocalizedMessage());
			}
		} // Production mode
		else {
			log.info("Added listener to port with config: FAKE setup");
		}

	}

	public void doThing() {
		Object[] data = new Object[8];
		data[0] = new LaunchEvent();
		data[1] = "B100:0:1:@";
		data[2] = "B100:0";
		data[3] = "B101:0";
		data[4] = "S80:0";
		data[5] = "B100:0";
		data[6] = "B100:0";
		data[7] = new ShutdownEvent();

		for (int i = 0; i < 8; i++) {
			long randomSeed = (long) (Math.random() * 10000);
			log.debug("Randomly selected pre-load time for event of " + randomSeed + "ms.");
			try {
				Thread.sleep(randomSeed);
			} catch (InterruptedException e) {
				log.error(e.getLocalizedMessage());
				log.error("Regardless of failed sleep, still continuing");
			}
			if (data[i].getClass() == String.class)
				dataReceived((String) data[i]);
			else {
				for (SpaceShipController ssc : listeners)
					ssc.spaceEvent((SpaceEvent) data[i]);
			}
		}
	}

	public void dataReceived(String eventData) {
		decodeAndNotify(eventData);
	}
	@Override
	public void dataReceived(SerialDataEvent event) {
		String receivedData = null;
		try {
			receivedData = event.getAsciiString();
			log.info(receivedData);
		} catch (IOException e) {
			log.error("Unable to receive message. Discarding.");
			receivedData = "";
			event = null;
		}
		//split the string for newline characters. Run decode for each of the strings thus received.
		String[] newLinesep = new String[2];
		newLinesep = receivedData.split("\n");
		if (newLinesep[0].isEmpty())
			decodeAndNotify(newLinesep[0]);
		if (newLinesep[1].isEmpty())
			decodeAndNotify(newLinesep[1]);
		//if (receivedData != "")
		//	decodeAndNotify(receivedData);
	}

	/**
	 * @param receivedData
	 *            the /n delimited string received on the serial port The method
	 *            will attempt to deconstruct the message and translate this into
	 *            events. Button presses always start with 'B' Generic message
	 *            format is Xnnn:vvv Please note that any follow-up :-signs are
	 *            discarded
	 */
	private void decodeAndNotify(String receivedData) {
		String[] command = new String[2];
		log.info("Command received: " + receivedData);
		if (receivedData.startsWith("BEGIN"))
		{
			log.info("Opened serial comms");
			receivedData = "X000:00";
		}
		if (receivedData.startsWith("LAUNCH"))
		{
			log.info("Launch detected");
			for (SpaceShipController ssc : listeners)
				ssc.spaceEvent(new LaunchEvent());
			receivedData = "X000:00";
		}
		if (receivedData.startsWith("TERM"))
		{
			log.info("Launch detected");
			for (SpaceShipController ssc : listeners)
				ssc.spaceEvent(new ShutdownEvent());
			receivedData = "X000:00";
		}
		command = receivedData.split(":");
		log.debug("State info in command: " + command[1]);
		String ifaceType = command[0].substring(0, 1);
		String ifaceIndex = command[0].substring(1);
		String payload = command[1];
		switch (ifaceType) {
		case "B":
			buttonPressed(ifaceIndex);
			break;
		case "S":
			switchOperated(ifaceIndex,payload);
			break;
		case "R":
			rotaryEncoderChanged(ifaceIndex);
			break;
		case "M":
			masterCaution();
			break;
		default:
			log.info("Unable to decode. Discarding");
			break;
		}

	}

	private void masterCaution() {
		//ignore all substrings
		MasterCautionPressedEvent mcpe = new MasterCautionPressedEvent();
		notifyListeners(mcpe);
	}

	private void rotaryEncoderChanged(String substring) {
		//int buttonPressed = Integer.decode(substring);
		RotaryEncoderEvent ree = new RotaryEncoderEvent();
		notifyListeners(ree);
		
	}

	private void switchOperated(String substring, String newValue) {
		int switchOperated = Integer.decode(substring);
		log.debug("Switch " + switchOperated + " operated. New value: " + newValue);
		SwitchEvent se = new SwitchEvent(switchOperated, newValue);
		notifyListeners(se);
		
	}

	private void buttonPressed(String substring) {
		// substring is 1, 10 or 100
		int buttonPressed = Integer.decode(substring);
		log.debug("Button " + buttonPressed + " pressed.");
		ButtonPressEvent bpe = new ButtonPressEvent(buttonPressed);
		notifyListeners(bpe);
		//SoundEvent se = new SoundEvent(new SpaceShipMapping().getButton(buttonPressed));

	}
	private void notifyListeners(SpaceEvent event)
	{
		for (SpaceShipController ssc : listeners)
			ssc.spaceEvent(event);
	}

	public void writeSerial(String msg) {

		// use outputstream or writeln?
		if (serialPort != null) {
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
				
				log.error(e.getLocalizedMessage());
			} catch (IOException e) {
				
				log.error(e.getLocalizedMessage());
			}

		} else {
			log.info("Stopped listening");
		}
	}

}
