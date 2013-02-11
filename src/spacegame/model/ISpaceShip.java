package spacegame.model;

import java.util.List;

import com.jme3.scene.control.Control;


public interface ISpaceShip extends ISpacePhysicsObject, Control {
	
	
	List<? extends IShipEngine> getEngines();
	
	IShipEngine getEngine(int id);

	void stopAllEngines();
	
	EngineGroup getEngineGroup(String id);
	
}
