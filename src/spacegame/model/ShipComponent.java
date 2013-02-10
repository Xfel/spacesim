package spacegame.model;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public abstract class ShipComponent extends AbstractControl {

	public Ship getShip() {
		if (spatial == null) {
			return null;
		}
		return spatial.getControl(Ship.class);
	}

	@Override
	public void setSpatial(Spatial spatial) {
		if (getShip() != null) {
			detach(getShip());
		}
		super.setSpatial(spatial);
		if (getShip() != null) {
			attach(getShip());
		}
	}

	protected void attach(Ship ship) {

	}

	protected void detach(Ship ship) {

	}

}
