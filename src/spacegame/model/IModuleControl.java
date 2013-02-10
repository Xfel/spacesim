package spacegame.model;

import spacegame.model.structure.Module;

public interface IModuleControl {
	
	Module getModule();
	
	Ship getShip();
	
	void setShip(Ship ship);
	
}
