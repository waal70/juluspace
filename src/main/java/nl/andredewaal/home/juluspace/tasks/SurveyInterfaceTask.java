/**
 * 
 */
package nl.andredewaal.home.juluspace.tasks;

import java.util.TimerTask;

import nl.andredewaal.home.juluspace.SpaceShipController;

/**
 * @author awaal
 *
 */
public class SurveyInterfaceTask extends TimerTask {
	private SpaceShipController _scc = null;

	/**
	 * 
	 */
	public SurveyInterfaceTask(SpaceShipController scc) {
		super();
		_scc = scc;
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		if (_scc != null)
		{
			_scc.surveyInterface();
		}
	}


}
