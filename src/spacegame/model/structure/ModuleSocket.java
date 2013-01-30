package spacegame.model.structure;

import java.util.EnumSet;

import com.jme3.math.Transform;

public class ModuleSocket {
	
	private Transform location=new Transform();

	private EnumSet<ModuleType> allowedTypes = EnumSet.noneOf(ModuleType.class);

	private EnumSet<ModuleTier> allowedTiers = EnumSet.noneOf(ModuleTier.class);

	public boolean isAccepted(Module module) {
		return allowedTypes.contains(module.getType()) && allowedTiers.contains(module.getTier());
	}

	public Transform getLocation() {
		return location;
	}

	public void setLocation(Transform location) {
		this.location.set(location);
	}

}
