/**
 * 
 */
package nl.andredewaal.home.juluspace;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author awaal
 *
 */
public class SpaceShipMain {
	private static Logger log = LogManager.getLogger(SpaceShipMain.class);

	/**
	 * @param args
	 * @return
	 * @throws IOException
	 */
	public static synchronized void main(String[] args) throws IOException {
	log.info("Spaceship START...");
		new QuindarTone().intro();
		// process incoming events from the Arduino through the SpaceShipController
		SpaceShipController ssc = new SpaceShipController();

		while (true) {
			if (!ssc.isBusy())
				break;
			try {
				Thread.currentThread().join();
				//Thread.sleep(Consts.TERM_SLEEP_EXIT);
			} catch (InterruptedException e) {
				log.error(e.getLocalizedMessage());
			}
		}
		ssc = null;
		log.info(".....Spaceship END");
		//System.exit(0);

	}

}
