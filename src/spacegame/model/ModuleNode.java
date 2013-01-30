package spacegame.model;

import spacegame.model.structure.Module;
import spacegame.model.structure.ModuleSocket;
import spacegame.util.StringUtils;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

public abstract class ModuleNode extends Node {
	private Module module;
	
	private Ship ship;
	private ModuleSocket mountPoint;

	protected ModuleNode(Module module, AssetManager assets) {
		this.module = module;
		
		
		if(!StringUtils.isBlank(module.getModelName())){
			attachChild(assets.loadModel(module.getModelName()));
		}
	}
	
	public Module getModule() {
		return module;
	}
	
	public ModuleSocket getMountPoint() {
		return mountPoint;
	}
	
	public Ship getShip() {
		return ship;
	}
	
	protected void setMount(Ship ship, ModuleSocket mountPoint){
		this.ship = ship;
		this.mountPoint = mountPoint;
		
		if(mountPoint!=null){
			setLocalTransform(mountPoint.getLocation());
		}
	}
	
	
}
