/**
 * 
 */
package nl.andredewaal.home.juluspace.events;

/**
 * @author awaal
 * This class specifies the types of events that can occur, both simple and complex
 * The idea is that, according to SpaceEventType, the processing will be different
 * TODO: maybe introduce the concept of a scenario, wherein several events will be
 * scheduled. Apollo 13 scenario: STARTUP, COUNTDOWN, LAUNCH, HOTANKSTIR, MAINBBUSUNDERVOLT
 */
public enum SpaceEventType {
	BUTTON, SWITCH, ROTARY, MASTERCAUTION, STARTUP, SHUTDOWN, LAUNCH, COUNTDOWN, SOUND,
	LIGHTNINGSTRIKE, HOTANKSTIR, MAINBBUSUNDERVOLT

}
