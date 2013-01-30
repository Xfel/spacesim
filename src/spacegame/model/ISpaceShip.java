package spacegame.model;

import java.util.List;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public interface ISpaceShip {
	
	float getMass();
	
	// these two describe the ships local space
	Vector3f getLocation();
	Quaternion getRotation();
	
	/**
	 * is in local space
	 * @return
	 */
	Vector3f getLinearVelocity();
	
	Quaternion getAngularVelocity();
	
//	PhysicsRigidBody getPhysicsObject();
	
	List<? extends IShipEngine> getEngines();
	
}
