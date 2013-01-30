package test;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

public class TestModelLoading extends SimpleApplication implements
		PhysicsTickListener, ActionListener {

	public static void main(String[] args) {
        TestModelLoading app = new TestModelLoading();
        app.setShowSettings(false);

        AppSettings settings=new AppSettings(true);
        settings.setSamples(4);
        app.start();
    }
	
	@Override
	public void simpleInitApp() {
		//Spatial ship = assetManager.loadModel("/Models/Complete/FirstShip/FirstShip.j3o");
		Spatial ship = assetManager.loadModel("/Models/Complete/FirstShip/FirstShip.blend");
		
		rootNode.attachChild(ship);
		
		
		
		
		SpotLight spot = new SpotLight();
		spot.setSpotRange(10);
		spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD);
		spot.setSpotOuterAngle(15f * FastMath.DEG_TO_RAD);
		
		spot.setPosition(new Vector3f(0, 0, 5));
		spot.setDirection(Vector3f.ZERO.subtract(spot.getPosition()).normalize());
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
	}
	
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void physicsTick(PhysicsSpace space, float tpf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void prePhysicsTick(PhysicsSpace space, float tpf) {
		// TODO Auto-generated method stub

	}

}
