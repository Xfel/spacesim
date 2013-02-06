package spacegame.ai;

import spacegame.model.ISpaceShip;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class StaticAbsoluteWaypoint implements IWaypoint {

	private Vector3f position;

	private Vector3f heading;

	public StaticAbsoluteWaypoint() {
		this(Vector3f.ZERO, Vector3f.UNIT_X);
	}

	public StaticAbsoluteWaypoint(Vector3f position, Vector3f heading) {
		this.position = new Vector3f(position);
		this.heading = new Vector3f(heading).normalizeLocal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("StaticAbsoluteWaypoint [position=%s, heading=%s]", position, heading);
	}

	@Override
	public Vector3f getRelativeLocation(ISpaceShip ship) {
		return position.subtract(ship.getLocation());
	}

	@Override
	public Quaternion getRelativeRotation(ISpaceShip ship) {
		Vector3f localDH = ship.getRotation().inverse().mult(heading);

		Quaternion rot = new Quaternion();

		rot.fromAngleAxis(Vector3f.UNIT_X.angleBetween(localDH), Vector3f.UNIT_X.cross(localDH, localDH));

		return rot;
	}

	@Override
	public Vector3f getRelativeVelocity(ISpaceShip ship) {
		return new Vector3f();
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getHeading() {
		return heading;
	}

	public StaticAbsoluteWaypoint setPosition(Vector3f position) {
		this.position.set(position);
		return this;
	}

	public StaticAbsoluteWaypoint setHeading(Vector3f heading) {
		this.heading.set(heading);
		return this;
	}
}
