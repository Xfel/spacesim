package spacegame.model;

import java.security.AllPermission;

import org.bushe.swing.event.EventBus;

import spacegame.model.event.DamageableEvent;
import spacegame.model.structure.Structure;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class StructureControl extends AbstractControl implements PhysicsControl, IDamageable, ISpacePhysicsObject,
		PhysicsTickListener {

	private Structure structure;

	private float integrity;

	private RigidBodyControl physics;

	private Vector3f location;

	private Quaternion rotation;

	private Vector3f linearVelocity;

	private Quaternion angularVelocity;

	public StructureControl(Structure structure) {
		this.physics = new RigidBodyControl(structure.getMass());
		this.structure = structure;
		// init to max health
		this.integrity = structure.getStructuralIntegrity();
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		StructureControl ctrl = new StructureControl(structure);

		ctrl.integrity = integrity;

		ctrl.setSpatial(spatial);
		return ctrl;
	}

	protected void onDeath() {
		
		EventBus.publish(spatial.getName(), new DamageableEvent.Death(this));

		if (spatial != null) {
			spatial.removeFromParent();
			getPhysicsSpace().removeAll(spatial);
		} else {
			getPhysicsSpace().remove(this);
		}

	}

	public Structure getStructure() {
		return structure;
	}
	
	public RigidBodyControl getPhysics() {
		return physics;
	}

	@Override
	public float getIntegrity() {
		return integrity;
	}

	@Override
	public float getMaximumIntegrity() {
		return structure.getStructuralIntegrity();
	}

	@Override
	public void damage(float damage, DamageType type) {
		if (damage <= 0) {
			// no damage
			return;
		}

		EventBus.publish(spatial.getName(), new DamageableEvent.Damaged(this, damage, type));

		this.integrity -= damage;

		if (integrity <= 0) {
			integrity = 0;
			onDeath();
		}
	}

	@Override
	public void repair(float amount) {
		if (amount <= 0) {
			// no repair
			return;
		}

		EventBus.publish(spatial.getName(), new DamageableEvent.Repaired(this, amount));

		this.integrity = Math.min(structure.getStructuralIntegrity(), integrity + amount);

	}

	@Override
	public void setSpatial(Spatial spatial) {
		super.setSpatial(spatial);
		physics.setSpatial(spatial);
	}

	@Override
	protected void controlUpdate(float tpf) {
		physics.update(tpf);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		physics.render(rm, vp);
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
		// do nothing
	}

	@Override
	public PhysicsSpace getPhysicsSpace() {
		return physics.getPhysicsSpace();
	}

	@Override
	public void setPhysicsSpace(PhysicsSpace space) {
		if (getPhysicsSpace() != null) {
			getPhysicsSpace().removeTickListener(this);
		}
		physics.setPhysicsSpace(space);
		if (getPhysicsSpace() != null) {
			getPhysicsSpace().addTickListener(this);
		}
	}
}
