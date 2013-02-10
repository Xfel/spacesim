package spacegame.model;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public interface ISpacePhysicsObject {

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

}