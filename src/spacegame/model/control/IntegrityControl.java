package spacegame.model.control;

import java.io.IOException;
import java.io.InputStream;

import javax.print.DocFlavor.STRING;

import org.bushe.swing.event.EventBus;

import spacegame.model.DamageType;
import spacegame.model.IDamageable;
import spacegame.model.event.DamageableEvent;

import com.jme3.bullet.control.PhysicsControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class IntegrityControl implements IDamageable {

	public static final String UD_INTEGRITY = "integrity";

	private float maximumIntegrity;

	private Spatial spatial;

	public IntegrityControl(float maximumIntegrity) {
		this.maximumIntegrity = maximumIntegrity;
	}

	/**
	 * for serialization, do not use
	 */
	public IntegrityControl() {
		// only for serialization
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		IntegrityControl ic = new IntegrityControl(maximumIntegrity);
		ic.setSpatial(spatial);
		return ic;
	}

	@Override
	public float getIntegrity() {
		Float udIntegrity = spatial.getUserData(UD_INTEGRITY);
		if (udIntegrity == null) {
			return maximumIntegrity;
		}
		return udIntegrity.floatValue();
	}

	@Override
	public float getMaximumIntegrity() {
		return maximumIntegrity;
	}

	@Override
	public void damage(float damage, DamageType type) {
		if (damage <= 0) {
			// no damage
			return;
		}

		EventBus.publish(spatial.getName(), new DamageableEvent.Damaged(this, damage, type));

		float integrity = getIntegrity();

		integrity -= damage;

		if (integrity <= 0) {
			integrity = 0;
			onDeath();
		}

		spatial.setUserData(UD_INTEGRITY, integrity);
	}

	@Override
	public void repair(float amount) {
		if (amount <= 0) {
			// no repair
			return;
		}

		EventBus.publish(spatial.getName(), new DamageableEvent.Repaired(this, amount));

		float integrity = getIntegrity();

		integrity += amount;
		if (integrity >= maximumIntegrity) {
			integrity = maximumIntegrity;
		}

		spatial.setUserData(UD_INTEGRITY, integrity);
	}

	protected void onDeath() {

		EventBus.publish(spatial.getName(), new DamageableEvent.Death(this));

		spatial.removeFromParent();

		PhysicsControl physics = spatial.getControl(PhysicsControl.class);
		if (physics != null) {
			physics.getPhysicsSpace().removeAll(spatial);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.scene.control.Control#setSpatial(com.jme3.scene.Spatial)
	 */
	@Override
	public void setSpatial(Spatial spatial) {
		if (this.spatial != null && spatial != null) {
			throw new IllegalStateException("This control has already been added to a Spatial");
		}
		this.spatial = spatial;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.scene.control.Control#update(float)
	 */
	@Override
	public void update(float tpf) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jme3.scene.control.Control#render(com.jme3.renderer.RenderManager,
	 * com.jme3.renderer.ViewPort)
	 */
	@Override
	public void render(RenderManager rm, ViewPort vp) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.export.Savable#write(com.jme3.export.JmeExporter)
	 */
	@Override
	public void write(JmeExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(spatial, "spatial", null);
		oc.write(maximumIntegrity, "maximumIntegrity", 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.export.Savable#read(com.jme3.export.JmeImporter)
	 */
	@Override
	public void read(JmeImporter im) throws IOException {
		InputCapsule ic = im.getCapsule(this);

		spatial = (Spatial) ic.readSavable("spatial", null);
		maximumIntegrity = ic.readFloat("maximumIntegrity", 0);
	}

}
