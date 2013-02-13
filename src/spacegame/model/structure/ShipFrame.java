package spacegame.model.structure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spacegame.model.ShipControl;
import spacegame.model.StructureControl;
import spacegame.model.control.IntegrityControl;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

public class ShipFrame extends Structure {

	private Map<String, ModuleSocket> sockets=new HashMap<String, ModuleSocket>();
	
	private Map<String, AssetKey<Module>> preboundModules=new HashMap<String, AssetKey<Module>>();

	public Spatial createSpatial(AssetManager assets){
		Spatial model=assets.loadModel(getModelName());
		
		model.addControl(new ShipControl(this));
		model.addControl(new IntegrityControl(getIntegrity()));
		
		for (Map.Entry<String, AssetKey<Module>> entry : preboundModules.entrySet()) {
			Module module= assets.loadAsset(entry.getValue());
			
			model.getControl(ShipControl.class).addModule(entry.getKey(), module, assets);
		}
		
		return model;
	}
	
	public ModuleSocket getSocket(String id){
		return sockets.get(id);
	}
	
	public void addSocket(String id, ModuleSocket socket){
		if(socket.getId()!=null){
			throw new IllegalStateException("socket is already added to a ship");
		}
		if(sockets.containsKey(id)){
			throw new IllegalArgumentException("socket id duplication");
		}
		
		socket.setId(id);
		
		sockets.put(id, socket);
	}
	
	public void prebindModule(String id, String moduleResPath){
		preboundModules.put(id, new AssetKey<Module>(moduleResPath));
	}
	
}
