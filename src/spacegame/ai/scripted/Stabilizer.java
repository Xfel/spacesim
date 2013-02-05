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

		adjust(angles[1], getShip());
		adjust(angles[2], getShip());
	}

	public static void adjust(float angle, ISpaceShip ship) {
		if (angle > FastMath.ZERO_TOLERANCE) {
			ship.getEngineGroup(EngineGroup.ID_ROTATE_UP).setCurrentForce(1f);
			ship.getEngineGroup(EngineGroup.ID_ROTATE_DOWN).setCurrentForce(0f);
		} else if (angle < -FastMath.ZERO_TOLERANCE) {
			ship.getEngineGroup(EngineGroup.ID_ROTATE_UP).setCurrentForce(0f);
			ship.getEngineGroup(EngineGroup.ID_ROTATE_DOWN).setCurrentForce(1f);
		} else {
			ship.getEngineGroup(EngineGroup.ID_ROTATE_UP).setCurrentForce(0f);
			ship.getEngineGroup(EngineGroup.ID_ROTATE_DOWN).setCurrentForce(0f);
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

	public static void setEngineRotation(float angularVelo, ISpaceShip ship) {
		if (angularVelo > FastMath.ZERO_TOLERANCE) {
			ship.getEngineGroup(EngineGroup.ID_ROTATE_UP).setCurrentForce(
					angularVelo);
			ship.getEngineGroup(EngineGroup.ID_ROTATE_DOWN).setCurrentForce(0f);
		} else if (angularVelo < -FastMath.ZERO_TOLERANCE) {
			ship.getEngineGroup(EngineGroup.ID_ROTATE_UP).setCurrentForce(0f);
			ship.getEngineGroup(EngineGroup.ID_ROTATE_DOWN).setCurrentForce(
					-angularVelo);
		} else {
			ship.getEngineGroup(EngineGroup.ID_ROTATE_UP).setCurrentForce(0f);
			ship.getEngineGroup(EngineGroup.ID_ROTATE_DOWN).setCurrentForce(0f);
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
