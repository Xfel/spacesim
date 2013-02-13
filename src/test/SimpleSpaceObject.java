package test;

import spacegame.model.ISpacePhysicsObject;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class SimpleSpaceObject implements ISpacePhysicsObject, PhysicsTickListener, PhysicsCollisionListener {

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
//		physics.setUserObject(this);
	}

	public void attach(PhysicsSpace space){
		space.addCollisionObject(physics);
		space.addCollisionListener(this);
		space.addTickListener(this);
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

		// FIXME angles get clamped!
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
	public void prePhysicsTick(PhysicsSpace space, float f) {

	}

	public Node getNode() {
		return node;
	}

	public RigidBodyControl getPhysics() {
		return physics;
	}

	@Override
	public void collision(PhysicsCollisionEvent evt) {
		if (evt.getObjectA() == physics && evt.getObjectB().getUserObject() instanceof ISpacePhysicsObject) {
			onCollision((ISpacePhysicsObject) evt.getObjectB().getUserObject(), evt);
		} else if (evt.getObjectB() == physics && evt.getObjectA().getUserObject() instanceof ISpacePhysicsObject) {
			onCollision((ISpacePhysicsObject) evt.getObjectA().getUserObject(), evt);
		}
	}

	protected void onCollision(ISpacePhysicsObject object, PhysicsCollisionEvent evt) {
		// TODO Auto-generated method stub

	}

}