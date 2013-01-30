package spacegame.model;

import spacegame.model.structure.Module;
import spacegame.model.structure.ModuleSocket;
import spacegame.model.structure.ShipFrame;
import spacegame.util.StringUtils;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Ship extends Node {

	private ShipFrame frame;
	private RigidBodyControl physics;
	
	private CompoundCollisionShape outline;

	public Ship(ShipFrame frame, AssetManager assets) {
		this.frame = frame;

		if (!StringUtils.isBlank(frame.getModelName())) {
			attachChild(assets.loadModel(frame.getModelName()));
		}
		
		physics=new RigidBodyControl(frame.getMass());
		addControl(physics);
		
		outline=new CompoundCollisionShape();
		physics.setCollisionShape(outline);
		
		outline.addChildShape(frame.getOutline(), Vector3f.ZERO);
	}

	public ShipFrame getFrame() {
		return frame;
	}

	public void mount(ModuleSocket socket, ModuleNode node) {
		attachChild(node);
		node.setMount(this, socket);
		
		physics.setMass(physics.getMass()+node.getModule().getMass());
		
		CollisionShape childOutline=node.getModule().getOutline();
		
		Matrix3f childTransform=new Matrix3f();
		node.getLocalRotation().toRotationMatrix(childTransform);
		childTransform.scale(node.getLocalScale());
		outline.addChildShape(childOutline, node.getLocalTranslation(), childTransform);
	}

	public void dismount(ModuleSocket socket) {
		for (int i = 0; i < getQuantity(); i++) {
			Spatial child = getChild(i);
			if ((child instanceof ModuleNode) && ((ModuleNode) child).getMountPoint() == socket) {
				detachChildAt(i);
				((ModuleNode) child).setMount(null, null);
				Module module = ((ModuleNode) child).getModule();
				physics.setMass(physics.getMass()+module.getMass());
				outline.removeChildShape(module.getOutline());
				break;
			}
		}
	}
	
	public RigidBodyControl getPhysics() {
		return physics;
	}

}
