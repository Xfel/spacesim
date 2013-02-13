package spacegame.model.structure;

import java.io.IOException;

import spacegame.model.StructureControl;
import spacegame.model.control.IntegrityControl;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.scene.Spatial;

public class Structure implements Savable {

	private String id;
	
	private String modelName;
	private String name;
	private float mass;
	private float integrity;

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
	public float getIntegrity() {
		return integrity;
	}

	/**
	 * @param structuralIntegrity
	 *            the structuralIntegrity to set
	 */
	public void setIntegrity(float structuralIntegrity) {
		this.integrity = structuralIntegrity;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	
	public void setOutline(CollisionShape outline) {
		this.outline = outline;
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
		OutputCapsule caps = ex.getCapsule(this);

		caps.write(name, "name", null);
		caps.write(modelName, "model", null);
		caps.write(mass, "mass", 0);
		caps.write(integrity, "integrity", 0);

		caps.write(outline, "outline", null);
	}

	@Override
	public void read(JmeImporter im) throws IOException {
		InputCapsule caps = im.getCapsule(this);

		name = caps.readString("name", null);
		modelName = caps.readString("model", null);
		mass = caps.readFloat("mass", 0);
		integrity = caps.readFloat("integrity", 0);
		
		outline=(CollisionShape) caps.readSavable("outline", null);

	}
	
	public Spatial createSpatial(AssetManager assets){
		Spatial model=assets.loadModel(modelName);
		
		model.addControl(new StructureControl(this));
		model.addControl(new IntegrityControl(integrity));
		
		return model;
	}

}