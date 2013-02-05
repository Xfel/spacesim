package spacegame.ai.scripted;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import spacegame.ai.Autopilot;
import spacegame.model.IShipEngine;
import spacegame.model.ISpaceShip;

public class Rotator extends Autopilot {

	private Vector3f targetHeading;

	private boolean accel, decel;

	private Vector3f rotSpeed;

	private float angle;

	@Override
	public void update() {
		if (accel) {
			Vector3f currentHeading = getShip().getRotation().mult(Vector3f.UNIT_X);
			float angleBetween = targetHeading.angleBetween(currentHeading);

			if (angleBetween < angle / 2) {
				// stop accel
				accel = false;
				decel = true;
				getShip().stopAllEngines();
			}else{
				setRotationSpeed(getShip(),rotSpeed);
			}
		} else if (decel) {
			Vector3f currentHeading = getShip().getRotation().mult(Vector3f.UNIT_X);
			float angleBetween = targetHeading.angleBetween(currentHeading);
			if(angleBetween<FastMath.ZERO_TOLERANCE){
				decel=false;
				getShip().stopAllEngines();
			}else{
				setRotationSpeed(getShip(),rotSpeed.negate());
			}
		} else {

			ISpaceShip ship = getShip();
			Quaternion angv = ship.getAngularVelocity();

			float[] angles = angv.toAngles(null);

			Stabilizer.adjust(angles[1], ship.getEngine(2), ship.getEngine(7), ship.getEngine(3), ship.getEngine(6));
			Stabilizer.adjust(angles[2], ship.getEngine(4), ship.getEngine(9), ship.getEngine(5), ship.getEngine(8));
		}
	}

	private void setRotationSpeed(ISpaceShip ship, Vector3f angvel) {
		setEngineRotation(angvel.y, ship.getEngine(2), ship.getEngine(7), ship.getEngine(3), ship.getEngine(6));
		setEngineRotation(angvel.z, ship.getEngine(4), ship.getEngine(9), ship.getEngine(5), ship.getEngine(8));
	}

	private void setEngineRotation(float angularVelo, IShipEngine engineDown1,
			IShipEngine engineDown2, IShipEngine engineUp1, IShipEngine engineUp2) {
		if (angularVelo > FastMath.ZERO_TOLERANCE) {
			engineUp1.setCurrentForce(angularVelo);
			engineUp2.setCurrentForce(angularVelo);

			engineDown1.setCurrentForce(0);
			engineDown2.setCurrentForce(0);
		} else if (angularVelo < -FastMath.ZERO_TOLERANCE) {
			engineUp1.setCurrentForce(0);
			engineUp2.setCurrentForce(0);

			engineDown1.setCurrentForce(-angularVelo);
			engineDown2.setCurrentForce(-angularVelo);
		} else {
			engineUp1.setCurrentForce(0);
			engineUp2.setCurrentForce(0);

			engineDown1.setCurrentForce(0);
			engineDown2.setCurrentForce(0);
		}
	}

	@Override
	public void queueTask(Vector3f desiredHeading, Vector3f desiredPosition) {
		desiredHeading = desiredHeading.normalize();

		Vector3f currentHeading = getShip().getRotation().mult(Vector3f.UNIT_X);
		float angleBetween = desiredHeading.angleBetween(currentHeading);
		Vector3f normal = desiredHeading.cross(currentHeading);

		Quaternion rot = new Quaternion();
		rot.fromAngleAxis(angleBetween, normal);

		Quaternion halfrot = new Quaternion();
		halfrot.fromAngleAxis(angleBetween / 2, normal);

		accel = true;
		this.targetHeading = desiredHeading;
//		this.halfWayRot=halfrot.multLocal(currentHeading);
		this.angle = angleBetween;
		float[] angles = rot.toAngles(null);

		this.rotSpeed = new Vector3f(angles[0], angles[1], angles[2]).normalizeLocal();
	}

}
