package spacegame.model.event;

import com.jme3.math.Vector3f;

import spacegame.model.ISensor;

public class SensorDetectEvent extends ModelEvent {

	private static final long serialVersionUID = 156054059520580621L;

	// time in ms since the object was at the given point;
	private long age;

	private Vector3f location;

	private Vector3f speed;

	public SensorDetectEvent(ISensor source, long age, Vector3f location, Vector3f speed) {
		super(source);
		this.age = age;
		this.location = location;
		this.speed = speed;
	}

	@Override
	public ISensor getSource() {
		return (ISensor) super.getSource();
	}

	/**
	 * Sensor detection can only return the position were the objects have been
	 * some time ago.
	 * 
	 * @return the age
	 */
	public long getAge() {
		return age;
	}

	/**
	 * The relative location to the sensor.
	 * 
	 * @return the location
	 */
	public Vector3f getLocation() {
		return location;
	}

	/**
	 * The relative speed.
	 * 
	 * @return the speed
	 */
	public Vector3f getSpeed() {
		return speed;
	}

}
