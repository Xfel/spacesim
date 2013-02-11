package spacegame.model.structure;

import java.io.IOException;

import spacegame.model.StructureControl;
import spacegame.model.control.IntegrityControl;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Structure implements Savable {

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
	
	public void setOutline(CollisionShape outline) {
		this.outline = outline;
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
		OutputCapsule caps = ex.getCapsule(this);

		caps.write(name, "name", null);
		caps.write(modelName, "model", null);
		caps.write(mass, "mass", 0);
		caps.write(structuralIntegrity, "integrity", 0);

		caps.write(outline, "outline", null);
	}

	@Override
	public void read(JmeImporter im) throws IOException {
		InputCapsule caps = im.getCapsule(this);

		name = caps.readString("name", null);
		modelName = caps.readString("model", null);
		mass = caps.readFloat("mass", 0);
		structuralIntegrity = caps.readFloat("integrity", 0);
		
		outline=(CollisionShape) caps.readSavable("outline", null);

	}
	
	public Spatial createSpatial(AssetManager assets){
		Spatial model=assets.loadModel(modelName);
		
		model.addControl(new StructureControl(this));
		model.addControl(new IntegrityControl(structuralIntegrity));
		
		return model;
	}

}