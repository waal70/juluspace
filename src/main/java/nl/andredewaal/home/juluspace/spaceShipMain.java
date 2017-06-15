/**
 * 
 */
package nl.andredewaal.home.juluspace;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;






/**
 * @author awaal
 *
 */
public class spaceShipMain {
	private static Logger log = Logger.getLogger(spaceShipMain.class);
	

	/**
	 * @param args
	 * @return 
	 * @throws IOException 
	 */
	public static synchronized void main(String[] args) throws IOException {
		initLog4J();
		log.debug("Spaceship START...");
		//process incoming events from the Arduino through the SpaceShipController
		SpaceShipController ssc = new SpaceShipController();
		
		while (ssc.isBusy())
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.debug(".....Spaceship END");
		System.exit(0);

	}
	
	private static void initLog4J()
	{
		InputStream is = spaceShipMain.class.getResourceAsStream("/log4j.properties");
		PropertyConfigurator.configure(is);
	}

}
