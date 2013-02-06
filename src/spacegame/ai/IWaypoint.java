package spacegame.ai;

import spacegame.model.ISpaceShip;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public interface IWaypoint {
	
	Vector3f getRelativeLocation(ISpaceShip ship);
	
	Quaternion getRelativeRotation(ISpaceShip ship);
	
	Vector3f getRelativeVelocity(ISpaceShip ship);
	
}
