package spacegame.model.modules;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

import spacegame.model.structure.Module;
import spacegame.model.structure.ModuleType;

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
