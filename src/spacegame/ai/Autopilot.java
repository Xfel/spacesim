package spacegame.ai;

import com.jme3.math.Vector3f;

import spacegame.model.IShipComponent;
import spacegame.model.ISpaceShip;

public abstract class Autopilot implements IShipComponent {
	
	private ISpaceShip ship;
	
	protected Autopilot() {
	}
	
	public abstract void queueTask(Vector3f desiredHeading, Vector3f desiredPosition);
	
	/* (non-Javadoc)
	 * @see spacegame.ai.IShipComponent#getShip()
	 */
	@Override
	public final ISpaceShip getShip() {
		return ship;
	}
	
	/* (non-Javadoc)
	 * @see spacegame.ai.IShipComponent#setShip(spacegame.model.ISpaceShip)
	 */
	@Override
	public final void setShip(ISpaceShip ship) {
		if(this.ship!=null){
			detach(ship);
		}
		this.ship = ship;
		if(ship!=null){
			attach(ship);
		}
	}

	protected void attach(ISpaceShip ship) {
		
	}

	protected void detach(ISpaceShip ship) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see spacegame.ai.IShipComponent#update()
	 */
	@Override
	public abstract void update();
	
}
