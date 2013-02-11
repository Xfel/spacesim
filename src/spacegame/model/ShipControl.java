package spacegame.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spacegame.app.SpaceSimApp;
import spacegame.model.structure.Module;
import spacegame.model.structure.ModuleSocket;
import spacegame.model.structure.ShipFrame;
import spacegame.model.structure.Structure;
import spacegame.util.StringUtils;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
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

		node.setLocalTransform(socket.getLocation());

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

		IModuleControl control = modules.get(socket);
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

	@Override
	public List<? extends IShipEngine> getEngines() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IShipEngine getEngine(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stopAllEngines() {
		// TODO Auto-generated method stub

	}

	@Override
	public EngineGroup getEngineGroup(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
