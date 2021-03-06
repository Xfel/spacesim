package spacegame.ai.scripted;

import com.jme3.math.FastMath;

public class DynamicAdjuster {

	private float halfWay;
	private boolean accel, decel;
	private boolean reset;

	private float lastAccel, lastSpeed;

	private float computedAccelerationFactor;

	public DynamicAdjuster() {
		reset();
	}

	public void reset() {
		accel = false;
		decel = false;

		reset = true;
	}

	public float getAcceleration(float distance, float speed) {
		computedAccelerationFactor = FastMath.abs((lastSpeed - speed) / lastAccel);
//		System.out.println(computedAccelerationFactor);

		float accel = doGetAcceleration(distance, speed);

		if (accel < -1) {
			accel = -1;
		} else if (accel > 1) {
			accel = 1;
		}

		lastAccel = accel;
		lastSpeed = speed;
		return accel;
	}

	protected float doGetAcceleration(float distance, float speed) {
//		if (computedAccelerationFactor != 0 && FastMath.abs(speed) > FastMath.ZERO_TOLERANCE) {
//			float slowdownDist = FastMath.sqr(speed) / (2 * computedAccelerationFactor);
//			System.out.println("Sdd: " + slowdownDist + " Dist: " + distance);
//			if (FastMath.abs(distance - slowdownDist) < FastMath.ZERO_TOLERANCE) {
//				return -FastMath.sign(speed);
//			}
//		}

		if (reset
				|| (FastMath.abs(speed) <= FastMath.ZERO_TOLERANCE && FastMath.abs(distance) > 0.1)) {
			halfWay = distance / 2;

			accel = true;
			System.out.println("Start - Distance: " + distance + " HW:" + halfWay);

			reset = false;
			return halfWay;
		} else if (accel) {
//			System.out.println(distance + ", " + halfWay);
			if (hwHit(distance)) {
				System.out.println("Hit HW at: " + distance);
				accel = false;
				decel = true;
				if (FastMath.abs(speed*computedAccelerationFactor) < 1 && FastMath.abs(speed*computedAccelerationFactor) > 0) {
					return -speed*computedAccelerationFactor;
				}
				return -halfWay;
			}
			return halfWay;
		} else if (decel) {
//			System.out.println("SH:" + speed + "," + halfWay);
			if (FastMath.abs(distance) <= FastMath.ZERO_TOLERANCE && FastMath.abs(speed) <= FastMath.FLT_EPSILON) {
				decel = false;
			} else if (FastMath.sign(halfWay) != FastMath.sign(speed)) {
				// went too far
				decel = false;
				halfWay = distance / 2;

				accel = true;
				System.out.println("Change direction Distance: " + distance + " - HW:" + halfWay);
				return halfWay;
			} else {
				if (FastMath.abs(speed*computedAccelerationFactor) < 1 && FastMath.abs(speed*computedAccelerationFactor) > 0) {
					return -speed*computedAccelerationFactor;
				}
				return -halfWay;
			}
		}

		// is more effective in area <1
		return distance - speed;
	}

	private boolean hwHit(float distance) {
		if (halfWay < 0) {
			return distance > halfWay;
		} else {
			return distance < halfWay;
		}
	}
}
