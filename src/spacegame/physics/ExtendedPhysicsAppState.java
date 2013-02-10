package spacegame.physics;

import java.util.ArrayList;
import java.util.List;

import spacegame.model.IDamageable;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;

/**
 * An extended version of the bulletAppState that will also manage gravity
 * between objects, damage on collision,...
 * 
 * @author Felix Treede
 * 
 */
public class ExtendedPhysicsAppState extends BulletAppState implements PhysicsCollisionListener {

	private static final float G_CONSTANT = 6.6738480e-11f;

	private List<RigidBodyControl> relevantObjects = new ArrayList<RigidBodyControl>();

	private List<RigidBodyControl> influencedObjects = new ArrayList<RigidBodyControl>();

	private List<Vector3f> gravitations = new ArrayList<Vector3f>();

	@Override
	public void cleanup() {
		getPhysicsSpace().removeCollisionListener(this);
		super.cleanup();
	}

	@Override
	public void startPhysics() {
		super.startPhysics();
		getPhysicsSpace().addCollisionListener(this);
	}

	@Override
	public void prePhysicsTick(PhysicsSpace space, float f) {
		super.prePhysicsTick(space, f);
	}

	@Override
	public void physicsTick(PhysicsSpace space, float f) {
		super.physicsTick(space, f);
		for (RigidBodyControl object : influencedObjects) {

			Vector3f gravity = new Vector3f();
			Vector3f radius = new Vector3f();

			for (RigidBodyControl gsource : relevantObjects) {
				radius.set(object.getPhysicsLocation()).subtractLocal(gsource.getPhysicsLocation());
				float gaccel = gsource.getMass() * G_CONSTANT / radius.lengthSquared();

				gravity.add(radius.normalize().multLocal(gaccel));
			}

			object.setGravity(gravity);

		}
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		if (event.getNodeA().getControl(IDamageable.class) != null) {
			
		}
	}

}
