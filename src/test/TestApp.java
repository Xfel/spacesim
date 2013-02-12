package test;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

import javax.swing.JOptionPane;

import org.lwjgl.input.Keyboard;

import spacegame.ai.Autopilot;
import spacegame.ai.scripted.Rotator;
import spacegame.ai.scripted.Stabilizer;
import spacegame.model.EngineGroup;
import spacegame.model.IShipEngine;
import spacegame.model.ISpaceShip;
import spacegame.model.structure.ShipFrame;
import spacegame.physics.ExtendedPhysicsAppState;
import spacegame.util.NumpadFlyByCamera;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.BulletAppState.ThreadingType;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.Particle;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.influencers.DefaultParticleInfluencer;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

public class TestApp extends SimpleApplication implements PhysicsTickListener, ActionListener {

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);
		Logger.getLogger("spacesim").setLevel(Level.INFO);

		TestApp app = new TestApp();
		// app.setShowSettings(false);
		AppSettings settings = new AppSettings(true);

		settings.setTitle("SpaceGame");
		try {
			settings.load("SpaceGame");
		} catch (BackingStoreException e) {
			Logger.getGlobal().log(Level.WARNING, "Unable to load settings", e);
		}
		// settings.setHeight(value)

		app.setSettings(settings);
		app.start();
	}

	private BulletAppState physicsState;
	// private ParticleEmitter fire1, fire2;
	private ISpaceShip sp;
	private BitmapText linVeloText;
	private BitmapText angVeloText;
	private BitmapText headingText;
	private boolean activateAP;
	private Autopilot autopilot;
	private BitmapText apText;
	private ChaseCamera followCam;
	private Spatial shipSpatial;

	@Override
	public void simpleInitApp() {
		physicsState = new ExtendedPhysicsAppState();
		physicsState.setThreadingType(ThreadingType.PARALLEL);
		stateManager.attach(physicsState);
		// physicsState.getPhysicsSpace().enableDebug(assetManager);
		physicsState.getPhysicsSpace().setGravity(new Vector3f(0, 0, 0));
		physicsState.getPhysicsSpace().addTickListener(this);

		// viewPort.setBackgroundColor(ColorRGBA.Blue);

		flyCam=new NumpadFlyByCamera(cam);
		flyCam.setEnabled(true);
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(10f);
		
		stateManager.detach(stateManager.getState(FlyCamAppState.class));
		flyCam.registerWithInput(inputManager);
		

		 createShip();

//		sp = new SimpleSpaceShip(assetManager);
//
//		rootNode.attachChild(sp.getNode());
//
//		physicsState.getPhysicsSpace().add(physics = sp.getPhysics());
//		physicsState.getPhysicsSpace().addTickListener(sp);
//
////		sp.getNode().addControl(followCam);
//		
//		followCam=new ChaseCamera(cam, sp.getNode(), inputManager);
////		followCam=new CameraControl(cam, ControlDirection.SpatialToCamera);
//		followCam.setEnabled(false);
////		followCam.set
////		followCam.set

		cam.setLocation(new Vector3f(0, 8f, 12f));
		cam.lookAt(new Vector3f(2, 2, 0), Vector3f.UNIT_Y);
		
		createAsteroid(new Vector3f(20, 0, 0));
		createAsteroid(new Vector3f(10, 40, 0));
		createAsteroid(new Vector3f(0, 0, 30));

		inputManager.addListener(this, "autopilot", "reset", "drive", "rotateUp", "rotateDown", "rotateLeft",
				"rotateRight", "cam");
		inputManager.addMapping("drive", new KeyTrigger(Keyboard.KEY_SPACE));
		inputManager.addMapping("rotateUp", new KeyTrigger(Keyboard.KEY_UP));
		inputManager.addMapping("rotateDown", new KeyTrigger(Keyboard.KEY_DOWN));
		inputManager.addMapping("rotateLeft", new KeyTrigger(Keyboard.KEY_LEFT));
		inputManager.addMapping("rotateRight", new KeyTrigger(Keyboard.KEY_RIGHT));
		inputManager.addMapping("reset", new KeyTrigger(Keyboard.KEY_X));
		inputManager.addMapping("cam", new KeyTrigger(Keyboard.KEY_C));
		inputManager.addMapping("autopilot", new KeyTrigger(Keyboard.KEY_P));

		final ScreenshotAppState state = new ScreenshotAppState();
		stateManager.attach(state);

		rootNode.attachChild(SkyFactory.createSky(assetManager,
				assetManager.loadTexture("Textures/Background/result_0_1.png"),
				assetManager.loadTexture("Textures/Background/result_2_1.png"),
				assetManager.loadTexture("Textures/Background/result_1_1.png"),
				assetManager.loadTexture("Textures/Background/result_3_1.png"),
				assetManager.loadTexture("Textures/Background/result_1_0.png"),
				assetManager.loadTexture("Textures/Background/result_1_2.png")));
		// viewPort.setClearFlags(false, false, false);

		Vector3f lightDirection = new Vector3f(0, 0, -1);

		DirectionalLight light = new DirectionalLight();
		// light.setColor(new ColorRGBA(0.95f, 1.0f, 1.0f, 1f));
		light.setDirection(lightDirection);
		rootNode.addLight(light);

		// PssmShadowRenderer bsr = new PssmShadowRenderer(assetManager, 256,
		// 3);
		// bsr.setDirection(lightDirection.normalizeLocal());
		// viewPort.addProcessor(bsr);

		// AmbientLight bgLight=new AmbientLight();
		// // bgLight.setColor(new ColorRGBA(0.25f, 0.25f, 0.25f, 1f));
		// rootNode.addLight(bgLight);

		createHUD();

		autopilot = new Rotator();
//		autopilot.setShip(sp);
	}

	private void createShip() {
		
		ShipFrame frame=new ShipFrame();
		
		frame.setMass(20f);
		frame.setModelName("Models/Complete/FirstShip/FirstShip_LowPoly.blend");
		frame.setName("FirstShip");
		frame.setStructuralIntegrity(2000f);
		
		
		shipSpatial=frame.createSpatial(assetManager);
		rootNode.attachChild(shipSpatial);
		physicsState.getPhysicsSpace().addAll(shipSpatial);
		
		sp=shipSpatial.getControl(ISpaceShip.class);
	}

	private void createAsteroid(Vector3f vector3f) {
		Spatial aster = assetManager.loadModel("Models/Asteroids/rock_textured.blend");

		// getChild to exclude blender's light and camera
		SimpleSpaceObject astObj = new SimpleSpaceObject(aster, 40);

		rootNode.attachChild(astObj.getNode());
		astObj.getPhysics().setPhysicsLocation(vector3f);

		physicsState.getPhysicsSpace().add(astObj.getPhysics());
		physicsState.getPhysicsSpace().addTickListener(astObj);

		astObj.getPhysics().setAngularVelocity(
				new Vector3f(FastMath.nextRandomFloat() * 0.5f, FastMath.nextRandomFloat() * 0.5f, FastMath
						.nextRandomFloat() * 0.5f));
	}

	private void createHUD() {
		linVeloText = new BitmapText(guiFont);
		linVeloText.setLocalTranslation(0, settings.getHeight(), 0);
		linVeloText.setText("Linear Velocity:");
		guiNode.attachChild(linVeloText);

		angVeloText = new BitmapText(guiFont);
		angVeloText.setLocalTranslation(0, settings.getHeight() - linVeloText.getLineHeight(), 0);
		angVeloText.setText("Angular Velocity:");
		guiNode.attachChild(angVeloText);

		headingText = new BitmapText(guiFont);
		headingText.setLocalTranslation(0,
				settings.getHeight() - linVeloText.getLineHeight() - angVeloText.getLineHeight(), 0);
		headingText.setText("Heading:");
		guiNode.attachChild(headingText);

		apText = new BitmapText(guiFont);
		apText.setLocalTranslation(0, settings.getHeight() - linVeloText.getLineHeight() - angVeloText.getLineHeight()
				- headingText.getLineHeight(), 0);
		apText.setText("Autopilot: off");
		guiNode.attachChild(apText);
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);

		Vector3f linearVelocity = sp.getLinearVelocity();
		linVeloText.setText(String.format("Linear Velocity: (%.2f, %.2f, %.2f)", linearVelocity.x, linearVelocity.y,
				linearVelocity.z));
		float[] angles = sp.getAngularVelocity().toAngles(null);
		// StringBuilder sb = new StringBuilder("(");
		// for (int i = 0; i < angles.length; i++) {
		// sb.append(angles[i] * FastMath.RAD_TO_DEG);
		// sb.append(", ");
		// }
		// sb.delete(sb.length() - 2, sb.length());
		// sb.append(')');

		angVeloText.setText(String.format("Angular Velocity: (%.2f, %.2f, %.2f)", angles[0] * FastMath.RAD_TO_DEG,
				angles[1] * FastMath.RAD_TO_DEG, angles[2] * FastMath.RAD_TO_DEG));

		Vector3f heading = sp.getRotation().mult(Vector3f.UNIT_X);
		headingText.setText(String.format("Heading: (%.2f, %.2f, %.2f)", heading.x, heading.y, heading.z));

		if (activateAP) {
//			autopilot.update();
		}
	}

	@Override
	public void prePhysicsTick(PhysicsSpace space, float f) {
		//
		// if (drive) {
		// physics.applyForce(physics.getPhysicsRotationMatrix().mult(new
		// Vector3f(1, 0, 0)), physics
		// .getPhysicsRotationMatrix().mult(new Vector3f(-1, 0, 0)));
		//
		// }
		// if (left) {
		//
		// physics.applyForce(physics.getPhysicsRotationMatrix().mult(new
		// Vector3f(0, 0, 1)), physics
		// .getPhysicsRotationMatrix().mult(new Vector3f(2, 0, 0)));
		// physics.applyForce(physics.getPhysicsRotationMatrix().mult(new
		// Vector3f(0, 0, -1)), physics
		// .getPhysicsRotationMatrix().mult(new Vector3f(-2, 0, 0)));
		// }else if (right) {
		//
		// physics.applyForce(physics.getPhysicsRotationMatrix().mult(new
		// Vector3f(0, 0, -1)), physics
		// .getPhysicsRotationMatrix().mult(new Vector3f(2, 0, 0)));
		// physics.applyForce(physics.getPhysicsRotationMatrix().mult(new
		// Vector3f(0, 0, 1)), physics
		// .getPhysicsRotationMatrix().mult(new Vector3f(-2, 0, 0)));
		// }
		//
		// if (up) {
		//
		// physics.applyForce(physics.getPhysicsRotationMatrix().mult(new
		// Vector3f(0, 1, 0)), physics
		// .getPhysicsRotationMatrix().mult(new Vector3f(2, 0, 0)));
		// physics.applyForce(physics.getPhysicsRotationMatrix().mult(new
		// Vector3f(0, -1, 0)), physics
		// .getPhysicsRotationMatrix().mult(new Vector3f(-2, 0, 0)));
		// }else if (down) {
		//
		// physics.applyForce(physics.getPhysicsRotationMatrix().mult(new
		// Vector3f(0, -1, 0)), physics
		// .getPhysicsRotationMatrix().mult(new Vector3f(2, 0, 0)));
		// physics.applyForce(physics.getPhysicsRotationMatrix().mult(new
		// Vector3f(0, 1, 0)), physics
		// .getPhysicsRotationMatrix().mult(new Vector3f(-2, 0, 0)));
		// }

		// if (next) {
		// physics.setAngularVelocity(new Vector3f(0, 0, 0.6f));
		// } else {
		// physics.setAngularVelocity(Vector3f.ZERO);
		//
		// }
	}

	@Override
	public void physicsTick(PhysicsSpace space, float f) {
		// System.out.println(physics.getAngularVelocity());
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("drive")) {
			if (isPressed) {
				sp.getEngineGroup(EngineGroup.ID_MAIN_DRIVE).setCurrentForce(1f);
			} else {
				sp.getEngineGroup(EngineGroup.ID_MAIN_DRIVE).setCurrentForce(0f);
			}
		} else if (name.equals("rotateUp")) {
			if (isPressed) {
				sp.getEngineGroup(EngineGroup.ID_ROTATE_UP).setCurrentForce(1f);
			} else {
				sp.getEngineGroup(EngineGroup.ID_ROTATE_UP).setCurrentForce(0f);
			}
		} else if (name.equals("rotateDown")) {
			if (isPressed) {
				sp.getEngineGroup(EngineGroup.ID_ROTATE_DOWN).setCurrentForce(1f);
			} else {
				sp.getEngineGroup(EngineGroup.ID_ROTATE_DOWN).setCurrentForce(0f);
			}
		} else if (name.equals("rotateLeft")) {
			if (isPressed) {
				sp.getEngineGroup(EngineGroup.ID_ROTATE_LEFT).setCurrentForce(1f);
			} else {
				sp.getEngineGroup(EngineGroup.ID_ROTATE_LEFT).setCurrentForce(0f);
			}
		} else if (name.equals("rotateRight")) {
			if (isPressed) {
				sp.getEngineGroup(EngineGroup.ID_ROTATE_RIGHT).setCurrentForce(1f);
			} else {
				sp.getEngineGroup(EngineGroup.ID_ROTATE_RIGHT).setCurrentForce(0f);
			}
		} else if (name.equals("reset")) {
//			sp.getPhysics().setAngularVelocity(Vector3f.ZERO);
//			sp.getPhysics().setLinearVelocity(Vector3f.ZERO);
//
//			sp.getPhysics().setPhysicsLocation(Vector3f.ZERO);
//			sp.getPhysics().setPhysicsRotation(Quaternion.IDENTITY);
		} else if (name.equals("autopilot")) {
			if (isPressed) {
				activateAP = !activateAP;
				if (activateAP) {
					String input = JOptionPane.showInputDialog("Enter target vector or cancel for stabilize");
					if (input != null) {
						String[] str = input.split(",");
						
						if(str.length!=3){
							JOptionPane.showMessageDialog(null, "You have to enter three vector components.", "Invalid input",
									JOptionPane.ERROR_MESSAGE);
							activateAP = false;
							return;
						}
						
						float x, y, z;

						try {
							x = Float.parseFloat(str[0]);
							y = Float.parseFloat(str[1]);
							z = Float.parseFloat(str[2]);
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(null, "Invalid number: " + e.getMessage(), "Invalid input",
									JOptionPane.ERROR_MESSAGE);
							activateAP = false;
							return;
						}
						autopilot.queueTask(new Vector3f(x, y, z), Vector3f.ZERO);
						autopilot.queueTask(Vector3f.UNIT_X, Vector3f.ZERO);
					}
					apText.setText("Autopilot: on");
				} else {
					autopilot.clearTask();
					sp.stopAllEngines();
					apText.setText("Autopilot: off");
				}

			}
		}else if(name.equals("cam")&&isPressed){
			
			boolean enable=flyCam.isEnabled();
			
			followCam.setEnabled(enable);
			flyCam.setEnabled(!enable);
		}
	}

}
