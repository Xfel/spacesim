package spacegame.util;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;

public class PhysicsHelper {
	public static void applyLocalForce(PhysicsRigidBody po, Vector3f force, Vector3f location) {
		
	}
	
	public static Vector3f getLocalLinearVelocity(PhysicsRigidBody po, Vector3f dst){
		if(dst==null){
			dst=new Vector3f();
		}
		
		po.getLinearVelocity(dst);
		
		po.getPhysicsRotation().inverseLocal().multLocal(dst);
		
		return dst;
	}
}
