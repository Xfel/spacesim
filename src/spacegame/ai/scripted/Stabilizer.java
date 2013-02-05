package spacegame.ai.scripted;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import spacegame.ai.Autopilot;
import spacegame.model.EngineGroup;
import spacegame.model.IShipEngine;
import spacegame.model.ISpaceShip;

public class Stabilizer extends Autopilot {

	@Override
	public void update() {
		Quaternion angv = getShip().getAngularVelocity();

		float[] angles = angv.toAngles(null);

		adjust(angles[0],getShip().getEngineGroup(EngineGroup.ID_SPIN_RIGHT),getShip().getEngineGroup(EngineGroup.ID_SPIN_LEFT));
		adjust(angles[1],getShip().getEngineGroup(EngineGroup.ID_ROTATE_RIGHT),getShip().getEngineGroup(EngineGroup.ID_ROTATE_LEFT));
		adjust(angles[2],getShip().getEngineGroup(EngineGroup.ID_ROTATE_DOWN),getShip().getEngineGroup(EngineGroup.ID_ROTATE_UP));

	}

	public static void adjust(float angle, EngineGroup rightDown, EngineGroup leftUp) {

		if (angle > FastMath.ZERO_TOLERANCE) {
			rightDown.setCurrentForce(1f);
			leftUp.setCurrentForce(0f);
		} else if (angle < -FastMath.ZERO_TOLERANCE) {
			rightDown.setCurrentForce(0f);
			leftUp.setCurrentForce(1f);
		} else {
			rightDown.setCurrentForce(0f);
			leftUp.setCurrentForce(0f);
		}
	}

	@Override
	public void queueTask(Vector3f desiredHeading, Vector3f desiredPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearTask() {
		// TODO Auto-generated method stub

	}

	public static void setEngineRotation(float angularVelo, EngineGroup rightDown, EngineGroup leftUp) {
		if (angularVelo > FastMath.ZERO_TOLERANCE) {
			rightDown.setCurrentForce(angularVelo);
			leftUp.setCurrentForce(0f);
		} else if (angularVelo < -FastMath.ZERO_TOLERANCE) {
			rightDown.setCurrentForce(0f);
			leftUp.setCurrentForce(angularVelo);
		} else {
			rightDown.setCurrentForce(0f);
			leftUp.setCurrentForce(0f);
		}
	}

	public static float getAccel(float current, float dst, float speed) {
		float desired = dst - current - speed;

		// System.out.println(dst+", "+speed+" -> "+desired);

		if (desired < -1) {
			desired = -1;
		} else if (desired > 1) {
			desired = 1;
		}

		return desired;
	}
}
