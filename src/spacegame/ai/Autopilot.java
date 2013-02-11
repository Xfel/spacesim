package spacegame.ai;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.math.Vector3f;

import spacegame.model.ISpaceShip;
import spacegame.model.ShipComponent;

public abstract class Autopilot extends ShipComponent {

	private static final Logger log=Logger.getLogger(Autopilot.class.getName());
	
	private ISpaceShip ship;

	private Queue<IWaypoint> waypoints;

	protected Autopilot() {
		waypoints = new LinkedBlockingQueue<IWaypoint>();
	}


	@Deprecated
	public void queueTask(Vector3f desiredHeading, Vector3f desiredPosition) {
		addWaypoint(new StaticAbsoluteWaypoint(desiredPosition, desiredHeading));
	}

	public void addWaypoint(IWaypoint wp) {
		waypoints.offer(wp);
	}

	public void clearTask() {
		waypoints.clear();
	}
	
	protected IWaypoint getNextWaypoint(){
		return waypoints.peek();
	}
	
	protected void waypointReached(){
		IWaypoint wp=waypoints.poll();
		System.out.println("Waypoint reached: "+wp);
		log.log(Level.INFO, "Waypoint reached: {0}", wp);
	}
}
