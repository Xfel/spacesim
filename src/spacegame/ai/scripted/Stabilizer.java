package spacegame.ai.scripted;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import spacegame.ai.Autopilot;
import spacegame.model.IShipEngine;

public class Stabilizer extends Autopilot {

	@Override
	public void update() {
		Quaternion angv = getShip().getAngularVelocity();

		float[] angles = angv.toAngles(null);

		adjust(angles[1], getShip().getEngine(2), getShip().getEngine(7), getShip().getEngine(3), getShip()
				.getEngine(6));
		adjust(angles[2], getShip().getEngine(4), getShip().getEngine(9), getShip().getEngine(5), getShip()
				.getEngine(8));
	}

	public static void adjust(float angle, IShipEngine engineDown1,
			IShipEngine engineDown2, IShipEngine engineUp1, IShipEngine engineUp2) {
		if (angle > FastMath.ZERO_TOLERANCE) {
			engineUp1.setCurrentForce(1f);
			engineUp2.setCurrentForce(1f);

			engineDown1.setCurrentForce(0);
			engineDown2.setCurrentForce(0);
		} else if (angle < -FastMath.ZERO_TOLERANCE) {
			engineUp1.setCurrentForce(0);
			engineUp2.setCurrentForce(0);

			engineDown1.setCurrentForce(1);
			engineDown2.setCurrentForce(1);
		} else {
			engineUp1.setCurrentForce(0);
			engineUp2.setCurrentForce(0);

			engineDown1.setCurrentForce(0);
			engineDown2.setCurrentForce(0);
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

	public static void setEngineRotation(float angularVelo, IShipEngine engineDown1,
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

	public static float getAccel(float current, float dst, float speed) {
		float desired = dst - current - speed;
	
		if (desired < -1) {
			desired= -1;
		} else if (desired > 1) {
			desired= 1;
		}
		
		return desired;
	}

}
