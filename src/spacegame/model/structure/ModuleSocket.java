package spacegame.model.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.jme3.math.Transform;

public class ModuleSocket {

	private String id;

	private Transform transform = new Transform();

	private EnumSet<ModuleType> allowedTypes = EnumSet.noneOf(ModuleType.class);

	private EnumSet<ModuleTier> allowedTiers = EnumSet.noneOf(ModuleTier.class);

	public boolean isAccepted(Module module) {
		return allowedTypes.contains(module.getType()) && allowedTiers.contains(module.getTier());
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform location) {
		this.transform.set(location);
	}

	public void setLocation(float x, float y, float z) {
		transform.setTranslation(x, y, z);
	}

	public String getId() {
		return id;
	}
	
	void setId(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModuleSocket other = (ModuleSocket) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Set<ModuleTier> getAllowedTiers() {
		return Collections.unmodifiableSet(allowedTiers);
	}

	public Set<ModuleType> getAllowedTypes() {
		return Collections.unmodifiableSet(allowedTypes);
	}

	public void setAllowedTiers(Set<ModuleTier> allowedTiers) {
		this.allowedTiers.clear();
		this.allowedTiers.addAll(allowedTiers);
	}

	public void setAllowedTypes(Set<ModuleType> allowedTypes) {
		this.allowedTypes.clear();
		this.allowedTypes.addAll(allowedTypes);
	}

	public void setAllowedTiers(List<String> allowedTiers) {
		this.allowedTiers.clear();
		for (String en : allowedTiers) {
			this.allowedTiers.add(ModuleTier.valueOf(en.toUpperCase()));
		}
	}

	public void setAllowedTypes(List<String> allowedTiers) {
		this.allowedTypes.clear();
		for (String en : allowedTiers) {
			this.allowedTypes.add(ModuleType.valueOf(en.toUpperCase()));
		}
	}
}
