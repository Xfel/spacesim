package spacegame.model;

import com.jme3.math.Vector3f;

public interface IShipEngine {
	
	ISpaceShip getShip();
	
	Vector3f getLocation();
	
	Vector3f getDirection();
	
	float getMaximumModulationAngle();
	
	float getMaximumForce();
	
	
	float getCurrentForce();
	
	/**
	 * in percent f max force (0-1)
	 * @param force
	 */
	void setCurrentForce(float force);
	
	
	Vector3f getActualDirection();
	
	void setActualDirection(Vector3f actualDir);
	
}
