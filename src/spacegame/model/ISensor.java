package spacegame.model;

import java.util.List;

/**
 * A generic sensor interface.
 * 
 * Sensors will detect all other objects within {@link #getActiveRange()} when active.
 * 
 * Additionally, they will detect objects with active sensors within {@link #getPassiveRange()} even when not active.
 * 
 * Visual sensors are a special case, as they can also operate using other light sources.
 * 
 * @author Felix Treede
 *
 */
public interface ISensor {
	
	enum Type {
		Visual,
		Radar,
	}
	ISpaceShip getShip();
	
	Type getType();
	
	float getActiveRange();
	
	float getPassiveRange();
	
	boolean isEnabled();
	
	boolean isActive();
	
	void setEnabled(boolean en);
	
	void setActive(boolean en);
	
	/**
	 * Starts a sensor sweep. Results will be reported as events.
	 */
	void sweep();
	
}
