package spacegame.model;

import com.jme3.scene.control.Control;

public interface IDamageable extends Control{
	
	float getIntegrity();
	
	float getMaximumIntegrity();
	
	void damage(float damage, DamageType type);
	
	void repair(float amount);
	
}
