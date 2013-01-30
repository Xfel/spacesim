package spacegame.model;

import java.util.ArrayList;
import java.util.List;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;

public class GravityManager implements PhysicsTickListener {

	private static final float G_CONSTANT = 6.6738480e-11f;

	private List<RigidBodyControl> relevantObjects=new ArrayList<RigidBodyControl>();
	
	private List<RigidBodyControl> influencedObjects=new ArrayList<RigidBodyControl>();
	
	private List<Vector3f> gravitations=new ArrayList<Vector3f>();
	
	@Override
	public void prePhysicsTick(PhysicsSpace space, float f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void physicsTick(PhysicsSpace space, float f) {
		for(RigidBodyControl object:influencedObjects){
			
			Vector3f gravity =new Vector3f();
			Vector3f radius=new Vector3f();
			
			for(RigidBodyControl gsource:relevantObjects){
				radius.set(object.getPhysicsLocation()).subtractLocal(gsource.getPhysicsLocation());
				float gaccel = gsource.getMass()*G_CONSTANT/radius.lengthSquared();
				
				gravity.add(radius.normalize().multLocal(gaccel));
			}
			
			object.setGravity(gravity);
			
		}
	}

}
