package spacegame.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class EngineGroup {
	
	public static final String ID_MAIN_DRIVE="main";
	
	public static final String ID_ROTATE_LEFT="rotate.left";
	public static final String ID_ROTATE_RIGHT="rotate.right";
	public static final String ID_ROTATE_UP="rotate.up";
	public static final String ID_ROTATE_DOWN="rotate.down";
	public static final String ID_SPIN_LEFT="rotate.lspin";
	public static final String ID_SPIN_RIGHT="rotate.rspin";

	
	public static final String ID_DRIFT_LEFT="drift.left";
	public static final String ID_DRIFT_RIGHT="drift.right";
	public static final String ID_DRIFT_UP="drift.up";
	public static final String ID_DRIFT_DOWN="drift.down";
	
	public static EngineGroup join(EngineGroup g1, EngineGroup g2){
		if(! g1.id.equals(g2.id)){
			throw new IllegalArgumentException("engine groups to join must have the same id.");
		}
		
		HashSet<IShipEngine> engines=new HashSet<IShipEngine>();
		engines.addAll(g1.engines);
		engines.addAll(g2.engines);
		
		return new EngineGroup(g1.getId(), engines);
	}
	
	private String id;

	private List<IShipEngine> engines;
	
	public EngineGroup(String id, Collection<IShipEngine> engines) {
		this.id = id;
		this.engines=new ArrayList<IShipEngine>(engines);
	}
	
	public EngineGroup(String id, IShipEngine...engines) {
		this.id = id;
		this.engines=Arrays.asList(engines);
	}
	
	public EngineGroup(String id, ISpaceShip ship, int...engineIds) {
		this.id = id;
		this.engines=new ArrayList<IShipEngine>(engineIds.length);
		
		for (int i = 0; i < engineIds.length; i++) {
			this.engines.add(ship.getEngine(engineIds[i]));
		}
	}
	
	public String getId() {
		return id;
	}
	
	public List<IShipEngine> getEngines() {
		return Collections.unmodifiableList(engines);
	}
	
	public void setCurrentForce(float force){
		for(IShipEngine engine: engines){
			engine.setCurrentForce(force);
		}
	}
	
}
