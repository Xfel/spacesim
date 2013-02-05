package spacegame.model;

public enum DamageType {
	/**
	 * Damage from collisions, projectile impacts, ...
	 */
	Kinetic, 
	
	/**
	 * Damage from laser/energy weapons.
	 */
	Energetic,
	
	/**
	 * Damage from internal sources such as reactor failures.
	 */
	Internal
}
