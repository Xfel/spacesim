package spacegame.ai.scripted;

import com.jme3.math.FastMath;

public class DynamicAdjuster {

	private float halfWay;
	private boolean accel, decel;
	private boolean reset;
	
	public DynamicAdjuster() {
		reset();
	}

	public void reset() {
		accel = false;
		decel = false;

		reset = true;
	}

	public float getAcceleration(float distance, float speed) {

		if (reset
				|| (FastMath.abs(speed) <= FastMath.ZERO_TOLERANCE && FastMath.abs(distance) > FastMath.ZERO_TOLERANCE)) {
			halfWay = distance / 2;

			accel = true;
			System.out.println("Start - Distance: "+distance+" HW:"+halfWay);
			
			reset=false;
		} else if (accel) {
//			System.out.println(distance + ", " + halfWay);
			if (hwHit(distance)) {
				System.out.println("Hit HW at: "+distance);
				accel = false;
				decel = true;
				return -FastMath.sign(halfWay);
			}
			return FastMath.sign(halfWay);
		} else if (decel) {
//			System.out.println("SH:" + speed + "," + halfWay);
			if (FastMath.abs(distance) <= 4 && FastMath.abs(speed) <= FastMath.FLT_EPSILON) {
				decel = false;
			} else if (FastMath.sign(halfWay) != FastMath.sign(speed)) {
				// went too far
				decel = false;
				halfWay = distance / 2;

				accel = true;
				System.out.println("Change direction Distance: "+distance+" - HW:"+halfWay);
				return FastMath.sign(halfWay);
			} else {
				return -FastMath.sign(halfWay);
			}
		}

		// is more effective in area <1
		float desired = distance - speed;

		// System.out.println(dst+", "+speed+" -> "+desired);

		if (desired < -1) {
			desired = -1;
		} else if (desired > 1) {
			desired = 1;
		}

		return desired;
	}
	
	private boolean hwHit(float distance){
		if(halfWay<0){
			return distance>halfWay;
		}else{
			return distance<halfWay;
		}
	}
}
