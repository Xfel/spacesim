package spacegame.model;

import spacegame.model.structure.Module;

import com.jme3.scene.control.Control;

public interface IModuleControl extends Control{
	
	Module getModule();
	
	ShipControl getShip();
	
	void setShip(ShipControl ship);
	
}
