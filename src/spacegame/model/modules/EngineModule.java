package spacegame.model.modules;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;

import spacegame.model.EngineNode;
import spacegame.model.ModuleNode;
import spacegame.model.structure.Module;

public class EngineModule extends Module{
	
	private float maximumDriveForce;
	
	private Vector3f emitterLocation;
	
	private float modulationAngle;
	
	private float defaultEnergyPerNewton;
	
	private float particlespeedPerNewton;
	
	public float computeEnergyCost(float desiredDriveForce){
		return desiredDriveForce*defaultEnergyPerNewton;
	}

	/**
	 * @return the maximumDriveForce
	 */
	public float getMaximumDriveForce() {
		return maximumDriveForce;
	}

	/**
	 * @return the emitterLocation
	 */
	public Vector3f getEmitterLocation() {
		return emitterLocation;
	}

	/**
	 * @return the modulationAngle
	 */
	public float getModulationAngle() {
		return modulationAngle;
	}

	/**
	 * @return the defaultEnergyPerNewton
	 */
	public float getDefaultEnergyPerNewton() {
		return defaultEnergyPerNewton;
	}

	/**
	 * @return the particlespeedPerNewton
	 */
	public float getParticlespeedPerNewton() {
		return particlespeedPerNewton;
	}
	
	@Override
	public ModuleNode createNode(AssetManager assets) {
		return new EngineNode(this, assets);
	}
	
}
