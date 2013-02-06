package spacegame.ai.scripted;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import spacegame.ai.Autopilot;
import spacegame.ai.IWaypoint;
import spacegame.model.EngineGroup;
import spacegame.model.ISpaceShip;

public class Rotator extends Autopilot {

	@Override
	public void update() {
		IWaypoint wp = getNextWaypoint();

		ISpaceShip ship = getShip();
		Quaternion angv = ship.getAngularVelocity();

		float[] angles = angv.toAngles(null);

		if (wp != null) {
			float[] dsts = wp.getRelativeRotation(ship).toAngles(null);
			
			boolean finished = true;
			for (int i = 0; i < dsts.length; i++) {
				if (FastMath.abs(dsts[i]) > FastMath.ZERO_TOLERANCE || FastMath.abs(angles[i]) > FastMath.FLT_EPSILON) {
					finished = false;
					break;
				}
			}
			if (finished) {
				waypointReached();
				ship.stopAllEngines();
			}

			Stabilizer.setEngineRotation(-Stabilizer.getAccel(0, dsts[0], angles[0]),
					getShip().getEngineGroup(EngineGroup.ID_SPIN_RIGHT),
					getShip().getEngineGroup(EngineGroup.ID_SPIN_LEFT));
			Stabilizer.setEngineRotation(-Stabilizer.getAccel(0, dsts[1], angles[1]),
					getShip().getEngineGroup(EngineGroup.ID_ROTATE_RIGHT),
					getShip().getEngineGroup(EngineGroup.ID_ROTATE_LEFT));
			Stabilizer.setEngineRotation(-Stabilizer.getAccel(0, dsts[2], angles[2]),
					getShip().getEngineGroup(EngineGroup.ID_ROTATE_DOWN),
					getShip().getEngineGroup(EngineGroup.ID_ROTATE_UP));

			
		} else {

			Stabilizer.adjust(angles[0], getShip().getEngineGroup(EngineGroup.ID_SPIN_RIGHT),
					getShip().getEngineGroup(EngineGroup.ID_SPIN_LEFT));
			Stabilizer.adjust(angles[1], getShip().getEngineGroup(EngineGroup.ID_ROTATE_RIGHT), getShip()
					.getEngineGroup(EngineGroup.ID_ROTATE_LEFT));
			Stabilizer.adjust(angles[2], getShip().getEngineGroup(EngineGroup.ID_ROTATE_DOWN), getShip()
					.getEngineGroup(EngineGroup.ID_ROTATE_UP));
		}
	}

}
