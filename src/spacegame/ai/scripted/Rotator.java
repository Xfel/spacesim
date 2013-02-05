package spacegame.ai.scripted;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import spacegame.ai.Autopilot;
import spacegame.model.ISpaceShip;

public class Rotator extends Autopilot {

	private Vector3f targetHeading;

	private boolean doRotate;

	private Vector3f rotSpeed;

	private float angle;

	@Override
	public void update() {
		ISpaceShip ship = getShip();
		Quaternion angv = ship.getAngularVelocity();

		float[] angles = angv.toAngles(null);

		if (doRotate) {
			Vector3f localDH=ship.getRotation().inverse().mult(targetHeading);
			
			Quaternion rot=new Quaternion();
			
			rot.fromAngleAxis(Vector3f.UNIT_X.angleBetween(localDH), Vector3f.UNIT_X.cross(localDH, localDH));
			
			float[] dsts=rot.toAngles(null);
			
			Stabilizer.setEngineRotation(-Stabilizer.getAccel(0, dsts[1], angles[1]), ship.getEngine(2), ship.getEngine(7), ship.getEngine(3), ship.getEngine(6));
			Stabilizer.setEngineRotation(-Stabilizer.getAccel(0, dsts[2], angles[2]), ship.getEngine(4), ship.getEngine(9), ship.getEngine(5), ship.getEngine(8));
			
		} else {

			Stabilizer.adjust(angles[1], ship.getEngine(2), ship.getEngine(7), ship.getEngine(3), ship.getEngine(6));
			Stabilizer.adjust(angles[2], ship.getEngine(4), ship.getEngine(9), ship.getEngine(5), ship.getEngine(8));
		}
	}

//	private void setRotationSpeed(ISpaceShip ship, Vector3f angvel) {
//		Stabilizer.setEngineRotation(angvel.y, ship.getEngine(2), ship.getEngine(7), ship.getEngine(3), ship.getEngine(6));
//		Stabilizer.setEngineRotation(angvel.z, ship.getEngine(4), ship.getEngine(9), ship.getEngine(5), ship.getEngine(8));
//	}

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

		doRotate = true;
		this.targetHeading = desiredHeading;
//		this.halfWayRot=halfrot.multLocal(currentHeading);
		this.angle = angleBetween;
		float[] angles = rot.toAngles(null);

		this.rotSpeed = new Vector3f(angles[0], angles[1], angles[2]).normalizeLocal();
	}

	@Override
	public void clearTask() {
		doRotate = false;
	}

}
