package spacegame.model;

import spacegame.model.modules.EngineModule;
import spacegame.model.structure.ModuleSocket;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.Particle;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.influencers.DefaultParticleInfluencer;
import com.jme3.effect.influencers.ParticleInfluencer;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class EngineNode extends ModuleNode implements PhysicsTickListener {

	private class EngineParticleInfluencer extends DefaultParticleInfluencer {
		@Override
		protected void applyVelocityVariation(Particle particle) {
			particle.velocity.set(getInitialVelocity());
			
			if (shipPhysics != null) {
				particle.velocity.add(shipPhysics.getLinearVelocity());
			}
			
	        temp.set(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat());
	        temp.multLocal(2f);
	        temp.subtractLocal(1f, 1f, 1f);
	        temp.multLocal(getInitialVelocity().length());
	        particle.velocity.interpolate(temp, velocityVariation);
		}
	}

	private float currentForce;

	private RigidBodyControl shipPhysics;

	private EngineParticleInfluencer particleInfluencer = new EngineParticleInfluencer();
	
	private ParticleEmitter particleEmitter;

	public EngineNode(EngineModule module, AssetManager assets) {
		super(module, assets);

		if (module.getModelName() != null) {
			attachChild(assets.loadModel(module.getModelName()));
		}
		
		particleEmitter=new ParticleEmitter("EngineSmoke", ParticleMesh.Type.Triangle, 30);
		particleEmitter.setParticleInfluencer(particleInfluencer);
		
		Material mat_red = new Material(assets, 
	            "Common/MatDefs/Misc/Particle.j3md");
	    mat_red.setTexture("Texture", assets.loadTexture(
	            "Effects/Explosion/flame.png"));
	    particleEmitter.setMaterial(mat_red);
	    particleEmitter.setImagesX(2); 
	    particleEmitter.setImagesY(2); // 2x2 texture animation
	    particleEmitter.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
	    particleEmitter.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
	    
//	    particleEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(2, 0, 0));
//	    particleEmitter.setInWorldSpace(false);
	    particleEmitter.setStartSize(0.9f);
	    particleEmitter.setEndSize(1.2f);
	    particleEmitter.setGravity(0, 0, 0);
	    particleEmitter.setLowLife(1f);
	    particleEmitter.setHighLife(3f);
	    particleEmitter.getParticleInfluencer().setVelocityVariation(0.1f);
	    particleEmitter.setParticlesPerSec(0.0f);
		particleEmitter.emitAllParticles();
	}

	public float getCurrentForce() {
		return currentForce;
	}

	public ParticleInfluencer getEngineParticleInfluencer() {
		return particleInfluencer;
	}

	@Override
	public EngineModule getModule() {
		return (EngineModule) super.getModule();
	}

	@Override
	public void physicsTick(PhysicsSpace space, float f) {
		// TODO Auto-generated method stub

	}

	@Override
	public void prePhysicsTick(PhysicsSpace space, float f) {
		if (currentForce > 0) {
			shipPhysics.applyForce(getLocalTransform().transformVector(new Vector3f(0, currentForce, 0), null),
					getLocalTransform().transformVector(getModule().getEmitterLocation(), null));
		}
	}

	public void setCurrentForce(float currentForce) {
		if (this.currentForce == currentForce) {
			return;
		}
		if (currentForce > getModule().getMaximumDriveForce()) {
			currentForce = getModule().getMaximumDriveForce();
		} 

		if (currentForce <= 0) {
			currentForce = 0;
		    particleEmitter.setParticlesPerSec(0.0f);
		}else{
		    particleEmitter.setParticlesPerSec(2.0f);
		}
		this.currentForce = currentForce;

		particleInfluencer
				.setInitialVelocity(new Vector3f(0, currentForce * getModule().getParticlespeedPerNewton(), 0));
	}
	
	@Override
	protected void setMount(Ship ship, ModuleSocket mountPoint) {
		if (shipPhysics != null) {
			shipPhysics.getPhysicsSpace().removeTickListener(this);
		}

		super.setMount(ship, mountPoint);
		
		if (ship != null) {
			shipPhysics = ship.getControl(RigidBodyControl.class);
			shipPhysics.getPhysicsSpace().addTickListener(this);
		} else {
			shipPhysics = null;
		}
	}

}
