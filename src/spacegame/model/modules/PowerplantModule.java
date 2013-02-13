package spacegame.model.modules;

import spacegame.model.structure.Module;
import spacegame.model.structure.ModuleType;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

public class PowerplantModule extends Module {

	private float maxPowerOutput;

	@Override
	public Spatial createSpatial(AssetManager assets) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModuleType getType() {
		return ModuleType.POWERPLANT;
	}

}
