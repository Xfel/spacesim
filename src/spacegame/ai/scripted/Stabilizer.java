package spacegame.ai.scripted;

import spacegame.ai.Autopilot;
import spacegame.model.EngineGroup;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class Stabilizer extends Autopilot {


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

	

	public static void setEngineRotation(float angularVelo, EngineGroup rightDown, EngineGroup leftUp) {
		if (angularVelo > FastMath.ZERO_TOLERANCE) {
			rightDown.setCurrentForce(angularVelo);
			leftUp.setCurrentForce(0f);
		} else if (angularVelo < -FastMath.ZERO_TOLERANCE) {
			rightDown.setCurrentForce(0f);
			leftUp.setCurrentForce(-angularVelo);
		} else {
			rightDown.setCurrentForce(0f);
			leftUp.setCurrentForce(0f);
		}
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void controlUpdate(float tpf) {
		Quaternion angv = getShip().getAngularVelocity();

		float[] angles = angv.toAngles(null);

		adjust(angles[0],getShip().getEngineGroup(EngineGroup.ID_SPIN_RIGHT),getShip().getEngineGroup(EngineGroup.ID_SPIN_LEFT));
		adjust(angles[1],getShip().getEngineGroup(EngineGroup.ID_ROTATE_RIGHT),getShip().getEngineGroup(EngineGroup.ID_ROTATE_LEFT));
		adjust(angles[2],getShip().getEngineGroup(EngineGroup.ID_ROTATE_DOWN),getShip().getEngineGroup(EngineGroup.ID_ROTATE_UP));
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub
		
	}
}
