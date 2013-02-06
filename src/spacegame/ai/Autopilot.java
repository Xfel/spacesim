package spacegame.ai;

import java.util.LinkedList;
import java.util.Queue;

import com.jme3.math.Vector3f;

import spacegame.model.IShipComponent;
import spacegame.model.ISpaceShip;

public abstract class Autopilot implements IShipComponent {

	private ISpaceShip ship;

	private Queue<IWaypoint> waypoints;

	protected Autopilot() {
		waypoints = new LinkedList<IWaypoint>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see spacegame.ai.IShipComponent#getShip()
	 */
	@Override
	public final ISpaceShip getShip() {
		return ship;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see spacegame.ai.IShipComponent#setShip(spacegame.model.ISpaceShip)
	 */
	@Override
	public final void setShip(ISpaceShip ship) {
		if (this.ship != null) {
			detach(ship);
		}
		this.ship = ship;
		if (ship != null) {
			attach(ship);
		}
	}

	protected void attach(ISpaceShip ship) {

	}

	protected void detach(ISpaceShip ship) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see spacegame.ai.IShipComponent#update()
	 */
	@Override
	public abstract void update();

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
		waypoints.poll();
	}
}
