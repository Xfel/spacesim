package spacegame.model;

import java.util.List;


public interface ISpaceShip extends ISpaceObject {
	
	
	List<? extends IShipEngine> getEngines();
	
	IShipEngine getEngine(int id);

	void stopAllEngines();
	
}
