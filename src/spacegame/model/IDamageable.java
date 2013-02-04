package spacegame.model;

public interface IDamageable {
	
	float getIntegrity();
	
	float getMaximumIntegrity();
	
	void damage(float damage, DamageType type);
	
	void repair(float amount);
	
}
