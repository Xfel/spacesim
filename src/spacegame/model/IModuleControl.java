package spacegame.model;

import com.jme3.scene.control.Control;

import spacegame.model.structure.Module;

public interface IModuleControl extends Control{
	
	Module getModule();
	
	ShipControl getShip();
	
	void setShip(ShipControl ship);
	
}
