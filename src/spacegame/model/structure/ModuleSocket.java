package spacegame.model.structure;

import java.util.EnumSet;

import com.jme3.math.Transform;

public class ModuleSocket {
	
	private String id;
	
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
	
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
	
	

}
