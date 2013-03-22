package spacegame.model.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spacegame.model.IShipEngine;
import spacegame.model.control.EngineControl;
import spacegame.model.control.IntegrityControl;
import spacegame.model.structure.Module;
import spacegame.model.structure.ModuleType;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class EngineModule extends Module {

	public static interface EnergyCostComputer{
		float compute(IShipEngine engine, float desiredDriveForce);
	}
	
	private float maximumDriveForce;

	private Vector3f emitterLocation = new Vector3f();

	private float modulationAngle;

	private float defaultEnergyPerNewton;

	private float particlespeedPerNewton;
	
	private EnergyCostComputer energyCostComputer;

	public float computeEnergyCost(IShipEngine engine, float desiredDriveForce) {
		if(energyCostComputer!=null){
			return energyCostComputer.compute(engine, desiredDriveForce);
		}
		return desiredDriveForce * defaultEnergyPerNewton;
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

	/**
	 * @param maximumDriveForce
	 *            the maximumDriveForce to set
	 */
	public void setMaximumDriveForce(float maximumDriveForce) {
		this.maximumDriveForce = maximumDriveForce;
	}

	/**
	 * @param emitterLocation
	 *            the emitterLocation to set
	 */
	public void setEmitterLocation(Vector3f emitterLocation) {
		this.emitterLocation.set(emitterLocation);
	}

	/**
	 * @param modulationAngle
	 *            the modulationAngle to set
	 */
	public void setModulationAngle(float modulationAngle) {
		this.modulationAngle = modulationAngle;
	}

	/**
	 * @param defaultEnergyPerNewton
	 *            the defaultEnergyPerNewton to set
	 */
	public void setDefaultEnergyPerNewton(float defaultEnergyPerNewton) {
		this.defaultEnergyPerNewton = defaultEnergyPerNewton;
	}

	/**
	 * @param particlespeedPerNewton
	 *            the particlespeedPerNewton to set
	 */
	public void setParticlespeedPerNewton(float particlespeedPerNewton) {
		this.particlespeedPerNewton = particlespeedPerNewton;
	}

	private Set<String> engineGroups = new HashSet<String>();

	public List<String> getEngineGroups() {
		return new ArrayList<String>(engineGroups);
	}

	public void setEngineGroups(List<String> engineGroups) {
		this.engineGroups.clear();
		this.engineGroups.addAll(engineGroups);
	}

	@Override
	public Spatial createSpatial(AssetManager assets) {
		Spatial model = assets.loadModel(getModelName());

//		model.addControl(new StructureControl(this));
		model.addControl(new IntegrityControl(getIntegrity()));
		model.addControl(new EngineControl(this));

		return model;
	}

	@Override
	public ModuleType getType() {
		return ModuleType.ENGINE;
	}

	public EnergyCostComputer getEnergyCostComputer() {
		return energyCostComputer;
	}

	public void setEnergyCostComputer(EnergyCostComputer energyCostComputer) {
		this.energyCostComputer = energyCostComputer;
	}
}
