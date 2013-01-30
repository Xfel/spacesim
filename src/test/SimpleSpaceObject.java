package test;

import spacegame.model.ISpaceObject;
import spacegame.model.ISpaceShip;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class SimpleSpaceObject implements ISpaceObject, PhysicsTickListener {

	protected Node node;
	protected RigidBodyControl physics;
	protected Quaternion rotation = new Quaternion();
	protected Vector3f location = new Vector3f();
	protected Quaternion angularVelocity = new Quaternion();
	protected Vector3f linearVelocity = new Vector3f();

	public SimpleSpaceObject(Spatial model, float mass) {
		node = new Node("FirstShip");

		node.attachChild(model);

		physics = new RigidBodyControl(CollisionShapeFactory.createDynamicMeshShape(model), mass);
//		physics.setDamping(0, 0);
//		physics.setFriction(0);
		physics.setAngularSleepingThreshold(0);
		node.addControl(physics);
	}

	@Override
	public float getMass() {
		return physics.getMass();
	}

	@Override
	public Vector3f getLocation() {
		return location;
	}

	@Override
	public Quaternion getRotation() {
		return rotation;
	}

	@Override
	public Vector3f getLinearVelocity() {
		return linearVelocity;
	}

	@Override
	public Quaternion getAngularVelocity() {
		return angularVelocity;
	}

	@Override
	public void physicsTick(PhysicsSpace space, float f) {
		physics.getPhysicsLocation(location);
		physics.getPhysicsRotation(rotation);

		// get angular velocity and convert it to local space
		// using linearVelocity as temporary here
		physics.getAngularVelocity(linearVelocity);
		angularVelocity.fromAngles(linearVelocity.getX(), linearVelocity.getY(), linearVelocity.getZ());
		float angle = angularVelocity.toAngleAxis(linearVelocity);
		rotation.inverse().multLocal(linearVelocity);
		angularVelocity.fromAngleNormalAxis(angle, linearVelocity);
		// angularVelocity.multLocal(rotation.inverse());

		// get linear velocity and convert it to local space
		physics.getLinearVelocity(linearVelocity);
		rotation.inverse().multLocal(linearVelocity);
	}

	@Override
	public void prePhysicsTick(PhysicsSpace arg0, float arg1) {

	}

	public Node getNode() {
		return node;
	}

	public RigidBodyControl getPhysics() {
		return physics;
	}

}