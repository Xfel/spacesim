package spacegame.model;

import spacegame.model.structure.Structure;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;

public class IntegrityControl extends RigidBodyControl implements PhysicsCollisionListener {

	private static final float IMPULSE_TO_DAMAGE_FACTOR=0.25f;
	
	private float integrity;

	public IntegrityControl(Structure structure) {
		super(structure.getOutline(), structure.getMass());

		// init to max health
		this.integrity = structure.getStructuralIntegrity();
	}

	public IntegrityControl(float mass, float integrity) {
		super(mass);
		this.integrity = integrity;
	}

	public IntegrityControl(CollisionShape shape, float mass, float integrity) {
		super(shape, mass);
		this.integrity = integrity;
	}

	@Override
	public void setPhysicsSpace(PhysicsSpace space) {
		if (this.space != null) {
			this.space.removeCollisionListener(this);
		}
		super.setPhysicsSpace(space);
		if (space != null) {
			space.addCollisionListener(this);
		}

	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		if (event.getObjectA() == this || event.getObjectB() == this) {
			float damage = event.getAppliedImpulse()*IMPULSE_TO_DAMAGE_FACTOR;
			
			this.damage(damage);
		}
	}

	/**
	 * Damages the object. If the integrity drops to zero, {@link #onDeath()} is called.
	 * 
	 * Override to allow custom damage processing.
	 * 
	 * @param damage
	 */
	public void damage(float damage) {
		if(damage<=0){
			// no damage
			return;
		}
		
		this.integrity-= damage;
		
		if(integrity<=0){
			integrity=0;
			onDeath();
		}
	}
	
	protected void onDeath(){
		
		if(spatial!=null){
			spatial.removeFromParent();
		}
		
	}
}
