package spacegame.model.control;

import java.util.List;

import spacegame.model.IModuleControl;
import spacegame.model.IShipEngine;
import spacegame.model.ShipControl;
import spacegame.model.modules.EngineModule;
import spacegame.model.structure.Module;

import com.jme3.effect.ParticleEmitter;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class EngineControl extends AbstractControl implements IModuleControl, IShipEngine {

	public static final String UD_ACTUAL_DIRECTION = "engine.direction";
	public static final String UD_CURRENT_FORCE = "engine.force";

	private EngineModule module;
	private ShipControl ship;
	private ParticleEmitter effect;

	public EngineControl(EngineModule module) {
		this.module = module;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		super.setSpatial(spatial);

		// try to find the particle emitter
		if (spatial instanceof ParticleEmitter) {
			effect = (ParticleEmitter) spatial;
		} else if (spatial instanceof Node) {
			Node node = (Node) spatial;

			List<ParticleEmitter> candidates = node.descendantMatches(ParticleEmitter.class);

			if (!candidates.isEmpty()) {
				effect = candidates.get(0);
			}
		}
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		EngineControl clone = new EngineControl(module);

		clone.setSpatial(spatial);

		return clone;
	}

	@Override
	public Module getModule() {
		return module;
	}

	@Override
	public ShipControl getShip() {
		return ship;
	}

	@Override
	public void setShip(ShipControl ship) {
		this.ship = ship;
	}

	@Override
	protected void controlUpdate(float tpf) {
		ship.getPhysics().applyForce(getActualDirection().mult(getCurrentForce() * getMaximumForce()), getLocation());
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector3f getLocation() {
		return spatial.getLocalTransform().transformVector(module.getEmitterLocation(), new Vector3f());
	}

	@Override
	public Vector3f getDirection() {
		return spatial.getLocalRotation().mult(Vector3f.UNIT_Y, new Vector3f());
	}

	@Override
	public Vector3f getActualDirection() {
		Vector3f userData = spatial.getUserData(UD_ACTUAL_DIRECTION);
		if (userData == null) {
			return getDirection();
		}
		return userData;
	}

	@Override
	public void setActualDirection(Vector3f actualDir) {
		actualDir.normalizeLocal();
		if (actualDir.angleBetween(getDirection()) > getMaximumModulationAngle()) {
			throw new IllegalArgumentException("Invalid direction");
		}

		spatial.setUserData(UD_ACTUAL_DIRECTION, actualDir);

		if (effect != null) {
			effect.getParticleInfluencer().setInitialVelocity(
					actualDir.mult(getCurrentForce() * module.getParticlespeedPerNewton()));
		}
	}

	@Override
	public float getMaximumForce() {
		return module.getMaximumDriveForce();
	}

	@Override
	public float getMaximumModulationAngle() {
		return module.getModulationAngle();
	}

	@Override
	public float getCurrentForce() {
		Float userData = spatial.getUserData(UD_CURRENT_FORCE);
		if (userData == null)
			return 0;
		return userData.floatValue();
	}

	@Override
	public void setCurrentForce(float force) {
		if (force < 0 || force > 1) {
			throw new IllegalArgumentException("Force out of supported bounds");
		}
		boolean wasNotZero = ((Float) spatial.getUserData(UD_CURRENT_FORCE)) > FastMath.FLT_EPSILON;
		spatial.setUserData(UD_CURRENT_FORCE, force);
		if (effect != null) {
			if (force > FastMath.FLT_EPSILON && !wasNotZero) {
				effect.setParticlesPerSec(20f);
			} else if (wasNotZero && force <= FastMath.FLT_EPSILON) {
				effect.setParticlesPerSec(0);
			}
			effect.getParticleInfluencer().setInitialVelocity(
					getActualDirection().mult(force * module.getParticlespeedPerNewton()));
		}
	}

}
