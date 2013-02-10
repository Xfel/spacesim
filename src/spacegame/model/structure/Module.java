package spacegame.model.structure;



public abstract class Module extends Structure{
	
	private ModuleType type;
	
	private ModuleTier tier;

	/**
	 * @return the type
	 */
	public ModuleType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ModuleType type) {
		this.type = type;
	}

	/**
	 * @return the tier
	 */
	public ModuleTier getTier() {
		return tier;
	}

	/**
	 * @param tier the tier to set
	 */
	public void setTier(ModuleTier tier) {
		this.tier = tier;
	}
	
	
	
}
