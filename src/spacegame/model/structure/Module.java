package spacegame.model.structure;



public abstract class Module extends Structure{
	
	private ModuleTier tier;

	/**
	 * @return the type
	 */
	public abstract ModuleType getType();

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
