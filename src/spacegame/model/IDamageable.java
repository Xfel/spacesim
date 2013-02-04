package spacegame.model;

public interface IDamageable {
	
	float getIntegrity();
	
	float getMaximumIntegrity();
	
	void damage(float damage);
	
	void repair(float amount);
	
}
