package test;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.Particle;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.influencers.DefaultParticleInfluencer;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import spacegame.model.IShipEngine;
import spacegame.model.ISpaceShip;

public class SimpleSpaceShip extends SimpleSpaceObject implements ISpaceShip {

	private class Engine extends DefaultParticleInfluencer implements IShipEngine {

		private Vector3f location, defaultDirection;
		private float maxModulationAngle, maxForce;

		private Vector3f actualDirection;
		private float currentForce;

		private ParticleEmitter pe;

		public Engine(AssetManager assets, Vector3f location, Vector3f defaultDirection, float maxModulationAngle,
				float maxForce) {
			this.location = location.clone();
			this.defaultDirection = defaultDirection.normalize();
			this.maxModulationAngle = maxModulationAngle;
			this.maxForce = maxForce;

			currentForce = 0;
			actualDirection = defaultDirection.clone();

			pe = createParticleEmitter(assets);
			pe.setLocalTranslation(location);
			
			node.attachChild(pe);
		}

		ParticleEmitter createParticleEmitter(AssetManager assets) {
			ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
			Material mat_red = new Material(assets, "Common/MatDefs/Misc/Particle.j3md");
			mat_red.setTexture("Texture", assets.loadTexture("Effects/Explosion/flame.png"));
			fire.setMaterial(mat_red);
			fire.setImagesX(2);
			fire.setImagesY(2); // 2x2 texture animation
			fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
			fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow

			fire.setParticleInfluencer(this);

//			fire.getParticleInfluencer().setInitialVelocity(new Vector3f(-20, 0, 0));
//			fire.setInWorldSpace(false);
			fire.setGravity(0, 0, 0);

			if (maxForce >= 10) {
				fire.setStartSize(0.9f);
				fire.setEndSize(1.2f);
				fire.setLowLife(1f);
				fire.setHighLife(3f);
			} else {
				fire.setStartSize(0.1f);
				fire.setEndSize(0.15f);
				fire.setLowLife(1f);
				fire.setHighLife(2f);
			}
			fire.getParticleInfluencer().setVelocityVariation(0.05f);
			fire.setParticlesPerSec(0);
			return fire;
		}

		@Override
		public ISpaceShip getShip() {
			return SimpleSpaceShip.this;
		}

		@Override
		public Vector3f getLocation() {
			return location;
		}

		@Override
		public Vector3f getDirection() {
			return defaultDirection;
		}

		@Override
		public float getMaximumModulationAngle() {
			return maxModulationAngle;
		}

		@Override
		public float getMaximumForce() {
			return maxForce;
		}

		@Override
		public float getCurrentForce() {
			return currentForce;
		}

		@Override
		public void setCurrentForce(float force) {
			if (currentForce < 0 || currentForce > 1) {
				throw new IllegalArgumentException("Force out of supported bounds");
			}

			this.currentForce = force;
			if (force > FastMath.FLT_EPSILON) {
				pe.setParticlesPerSec(20f);
			} else {
				pe.setParticlesPerSec(0);
			}
		}

		@Override
		public Vector3f getActualDirection() {
			return actualDirection;
		}

		@Override
		public void setActualDirection(Vector3f actualDir) {
			actualDir.normalizeLocal();
			if (actualDir.angleBetween(defaultDirection) > maxModulationAngle) {
				throw new IllegalArgumentException("Invalid direction");
			}

			this.actualDirection = actualDir;

		}

		@Override
		protected void applyVelocityVariation(Particle particle) {
			particle.velocity.set(actualDirection);
			particle.velocity.multLocal(-currentForce);

			temp.set(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat());
			temp.multLocal(2f);
			temp.subtractLocal(1f, 1f, 1f);
			temp.multLocal(currentForce);
			particle.velocity.interpolate(temp, getVelocityVariation());

			if (physics != null) {
				Vector3f trueMoveDir = physics.getPhysicsRotation().inverseLocal().mult(physics.getLinearVelocity())
						.negate();

				particle.velocity.addLocal(particle.velocity.normalizeLocal().multLocal(
						FastMath.abs(trueMoveDir.dot(particle.velocity))));
			}

		}

	}

	private List<Engine> engines = new ArrayList<Engine>();

	public SimpleSpaceShip(AssetManager assets) {
		super(assets.loadModel("Models/Complete/FirstShip/FirstShip_LowPoly.blend"), 20f);
		
		//(AssetManager assets, Vector3f location, Vector3f defaultDirection, float maxModulationAngle,float maxForce
		
		// the main engines
		/* 0 */engines.add(new Engine(assets, new Vector3f(-3.6569f, 0, -2.2305f), new Vector3f(1, 0, 0), 0, 10f));
		/* 1 */engines.add(new Engine(assets, new Vector3f(-3.6569f, 0, 2.2305f), new Vector3f(1, 0, 0), 0, 10f));
		
		// the control engines
		//front right
		/* 2 */engines.add(new Engine(assets, new Vector3f(2.4243f, -0.326057f, -0.611425f), new Vector3f(0, 0, -1), 0, 2f));
		//front left
		/* 3 */engines.add(new Engine(assets, new Vector3f(2.4243f, -0.326057f, 0.611425f), new Vector3f(0, 0, 1), 0, 2f));
		//front top
		/* 4 */engines.add(new Engine(assets, new Vector3f(2.4598f, 0.094766f, 0f), new Vector3f(0, 1, 0), 0, 2f));
		//front bottom
		/* 5 */engines.add(new Engine(assets, new Vector3f(2.4747f, -0.821086f, 0f), new Vector3f(0, -1, 0), 0, 2f));
		//back right
		/* 6 */engines.add(new Engine(assets, new Vector3f(-3.329f, -0.119323f, -0.998017f), new Vector3f(0, 0, -1), 0, 2f));
		//back left
		/* 7 */engines.add(new Engine(assets, new Vector3f(-3.329f, -0.119323f, 0.998017f), new Vector3f(0, 0, 1), 0, 2f));
		//back top
		/* 8 */engines.add(new Engine(assets, new Vector3f(-2.4444f, 0.640549f, 0f), new Vector3f(0, 1, 0), 0, 2f));
		//back bottom
		/* 9 */engines.add(new Engine(assets, new Vector3f(-2.4569f, -0.732492f, 0f), new Vector3f(0, -1, 0), 0, 2f));
	}

	@Override
	public void prePhysicsTick(PhysicsSpace space, float f) {
		for (Engine engine : engines) {

			Vector3f force = engine.getActualDirection().mult(engine.getCurrentForce()*engine.getMaximumForce());

			physics.applyForce(rotation.multLocal(force), rotation.mult(engine.getLocation()));
		}
	}

	@Override
	public List<? extends IShipEngine> getEngines() {
		return engines;
	}
	
	@Override
	public IShipEngine getEngine(int id) {
		return engines.get(id);
	}

}
