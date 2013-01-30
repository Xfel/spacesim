package spacegame.model.structure;

import com.jme3.bullet.collision.shapes.CollisionShape;

public class Structure {

	private String modelName;
	private String name;
	private float mass;
	private float structuralIntegrity;
	
	private CollisionShape outline;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the mass
	 */
	public float getMass() {
		return mass;
	}

	/**
	 * @param mass
	 *            the mass to set
	 */
	public void setMass(float mass) {
		this.mass = mass;
	}

	/**
	 * @return the structuralIntegrity
	 */
	public float getStructuralIntegrity() {
		return structuralIntegrity;
	}

	/**
	 * @param structuralIntegrity
	 *            the structuralIntegrity to set
	 */
	public void setStructuralIntegrity(float structuralIntegrity) {
		this.structuralIntegrity = structuralIntegrity;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public CollisionShape getOutline() {
		return outline;
	}
	
	
}