/**
 * 
 */
package nl.andredewaal.home.juluspace.events;

import nl.andredewaal.home.juluspace.events.complex.*;

/**
 * @author awaal
 *
 */
public class ScenarioSingleton {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Apollo 13 scenario: STARTUP, COUNTDOWN, LAUNCH, HOTANKSTIR, MAINBBUSUNDERVOLT
		//This class prepopulates the Scenarios.
		
		SpaceScenario a13 = new SpaceScenario("Apollo 13", "This is the Apollo 13 scenario");
		//Create startup event:
		a13.add(new CountdownEvent());
		a13.add(new LaunchEvent());
		a13.add(new ShutdownEvent());
		
		};
		

	}


