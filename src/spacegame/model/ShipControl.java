package spacegame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spacegame.model.structure.Module;
import spacegame.model.structure.ModuleSocket;
import spacegame.model.structure.ShipFrame;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class ShipControl extends StructureControl implements ISpaceShip {

	private ShipFrame frame;

	private Map<ModuleSocket, IModuleControl> modules;

	public ShipControl(ShipFrame frame) {
		super(frame);
		this.frame = frame;
		modules = new HashMap<ModuleSocket, IModuleControl>();
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		ShipControl otherShip = new ShipControl(frame);

		otherShip.setSpatial(spatial);
		return otherShip;
	}

	@Override
	public ShipFrame getStructure() {
		return frame;
	}

	public void addModule(String socketId, Module module, AssetManager loader) {
		ModuleSocket socket = frame.getSocket(socketId);

		if (!socket.isAccepted(module)) {

		}

		Spatial node = module.createSpatial(loader);

		node.setName(socketId);

		node.setLocalTransform(socket.getTransform());

		((Node) getSpatial()).attachChild(node);

		IModuleControl control = node.getControl(IModuleControl.class);
		control.setShip(this);
		modules.put(socket, control);

		if (getPhysicsSpace() != null) {
			getPhysicsSpace().addAll(node);
		}
	}

	public void removeModule(String socketId) {
		ModuleSocket socket = frame.getSocket(socketId);

		IModuleControl control = modules.remove(socket);
		control.setShip(null);

		for (int i = 0; i < ((Node) getSpatial()).getQuantity(); i++) {
			Spatial child = ((Node) getSpatial()).getChild(i);
			if (child.getControl(IModuleControl.class) == control) {
				((Node) getSpatial()).detachChildAt(i);
				if (getPhysicsSpace() != null) {
					getPhysicsSpace().removeAll(child);
				}
			}
		}
	}
	
	private List<IShipEngine> engines=new ArrayList<IShipEngine>();
	
	private HashMap<String, EngineGroup> engineGroups=new HashMap<String, EngineGroup>();

	public void addEngine(IShipEngine engine, String...groups){
		engines.add(engine);
		
		for (String gid : groups) {
			getEngineGroup(gid).add(engine);
		}
	}
	
	public void removeEngine(IShipEngine engine){
		for(EngineGroup group:engineGroups.values()){
			group.remove(engine);
		}
		engines.remove(engine);
	}
	
	@Override
	public List<? extends IShipEngine> getEngines() {
		return Collections.unmodifiableList(engines);
	}

	@Override
	public IShipEngine getEngine(int id) {
		return engines.get(id);
	}

	@Override
	public void stopAllEngines() {
		for(IShipEngine engine: engines){
			engine.setCurrentForce(0);
		}
	}

	@Override
	public EngineGroup getEngineGroup(String id) {
		EngineGroup group=engineGroups.get(id);
		if(group==null){
			group=new EngineGroup(id);
			engineGroups.put(id, group);
		}
		return group;
	}

	public void applyLocalForce(Vector3f force, Vector3f location) {
		physics.applyForce(rotation.multLocal(force), rotation.mult(location));
	
	}

}
