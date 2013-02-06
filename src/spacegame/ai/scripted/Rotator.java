package spacegame.ai.scripted;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import spacegame.ai.Autopilot;
import spacegame.ai.IWaypoint;
import spacegame.model.EngineGroup;
import spacegame.model.ISpaceShip;

public class Rotator extends Autopilot {

	private DynamicAdjuster adjustAngleX=new DynamicAdjuster();
	private DynamicAdjuster adjustAngleY=new DynamicAdjuster();
	private DynamicAdjuster adjustAngleZ=new DynamicAdjuster();
	
	@Override
	public void update() {
		IWaypoint wp = getNextWaypoint();

		ISpaceShip ship = getShip();
		Quaternion angv = ship.getAngularVelocity();

		float[] speed = angv.toAngles(null);

		if (wp != null) {
			float[] dsts = wp.getRelativeRotation(ship).toAngles(null);
			
			boolean finished = true;
			for (int i = 0; i < dsts.length; i++) {
				if (FastMath.abs(dsts[i]) > FastMath.ZERO_TOLERANCE || FastMath.abs(speed[i]) > FastMath.FLT_EPSILON) {
					finished = false;
					break;
				}
			}
			if (finished) {
				waypointReached();
				ship.stopAllEngines();
			}

			Stabilizer.setEngineRotation(-adjustAngleX.getAcceleration(dsts[0], speed[0]),
					getShip().getEngineGroup(EngineGroup.ID_SPIN_RIGHT),
					getShip().getEngineGroup(EngineGroup.ID_SPIN_LEFT));
			Stabilizer.setEngineRotation(-adjustAngleY.getAcceleration( dsts[1], speed[1]),
					getShip().getEngineGroup(EngineGroup.ID_ROTATE_RIGHT),
					getShip().getEngineGroup(EngineGroup.ID_ROTATE_LEFT));
			Stabilizer.setEngineRotation(-adjustAngleZ.getAcceleration( dsts[2], speed[2]),
					getShip().getEngineGroup(EngineGroup.ID_ROTATE_DOWN),
					getShip().getEngineGroup(EngineGroup.ID_ROTATE_UP));

			
		} else {

			Stabilizer.adjust(speed[0], getShip().getEngineGroup(EngineGroup.ID_SPIN_RIGHT),
					getShip().getEngineGroup(EngineGroup.ID_SPIN_LEFT));
			Stabilizer.adjust(speed[1], getShip().getEngineGroup(EngineGroup.ID_ROTATE_RIGHT), getShip()
					.getEngineGroup(EngineGroup.ID_ROTATE_LEFT));
			Stabilizer.adjust(speed[2], getShip().getEngineGroup(EngineGroup.ID_ROTATE_DOWN), getShip()
					.getEngineGroup(EngineGroup.ID_ROTATE_UP));
		}
	}
	
	@Override
	protected void waypointReached() {
		super.waypointReached();
		
		adjustAngleX.reset();
		adjustAngleY.reset();
		adjustAngleZ.reset();
	}

	@Override
	public void clearTask() {
		super.clearTask();

		
		adjustAngleX.reset();
		adjustAngleY.reset();
		adjustAngleZ.reset();
	}
}
