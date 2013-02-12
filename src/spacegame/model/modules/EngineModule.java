package spacegame.model.modules;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import spacegame.model.structure.Module;
import spacegame.model.structure.ModuleType;

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
	public Spatial createSpatial(AssetManager assets) {
		return null;
	}
	@Override
	public ModuleType getType() {
		return ModuleType.ENGINE;
	}
}
