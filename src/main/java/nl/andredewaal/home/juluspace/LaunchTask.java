/**
 * 
 */
package nl.andredewaal.home.juluspace;

import java.util.TimerTask;

import nl.andredewaal.home.juluspace.events.CountdownEvent;

/**
 * @author awaal
 *
 */
public class LaunchTask extends TimerTask {
	private SpaceShipController _scc = null;

	/**
	 * 
	 */
	public LaunchTask(SpaceShipController scc)
	{
		super();
		_scc = scc;
		
	}
	public LaunchTask() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		if (_scc != null)
		{
			CountdownEvent ce = new CountdownEvent();
			_scc.spaceEvent(ce);
		}

	}

}
