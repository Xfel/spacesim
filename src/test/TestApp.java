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
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

public class TestApp extends SimpleApplication implements PhysicsTickListener, ActionListener {

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);

		TestApp app = new TestApp();
		app.setShowSettings(false);
		AppSettings settings = new AppSettings(true);

		settings.setTitle("SpaceGame");
		try {
			settings.load("SpaceGame");
		} catch (BackingStoreException e) {
			Logger.getGlobal().log(Level.WARNING, "Unable to load settings", e);
		}
//		settings.setHeight(value)

		app.setSettings(settings);
		app.start();
	}

	private BulletAppState physicsState;
	private RigidBodyControl physics;
//	private ParticleEmitter fire1, fire2;
	private SimpleSpaceShip sp;
	private BitmapText linVeloText;
	private BitmapText angVeloText;
	private BitmapText headingText;
	private boolean activateAP;
	private Autopilot autopilot;

	@Override
	public void simpleInitApp() {
		physicsState = new BulletAppState();
		physicsState.setThreadingType(ThreadingType.PARALLEL);
		stateManager.attach(physicsState);
//		physicsState.getPhysicsSpace().enableDebug(assetManager);
		physicsState.getPhysicsSpace().setGravity(new Vector3f(0, 0, 0));
		physicsState.getPhysicsSpace().addTickListener(this);

//		viewPort.setBackgroundColor(ColorRGBA.Blue);

		cam.setLocation(new Vector3f(0, 8f, 12f));
		cam.lookAt(new Vector3f(2, 2, 0), Vector3f.UNIT_Y);

		flyCam.setEnabled(true);
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(10f);

//		createShip();

		sp = new SimpleSpaceShip(assetManager);

		rootNode.attachChild(sp.getNode());

		physicsState.getPhysicsSpace().add(physics = sp.getPhysics());
		physicsState.getPhysicsSpace().addTickListener(sp);

		createAsteroid(new Vector3f(20, 0, 0));
		createAsteroid(new Vector3f(10, 40, 0));
		createAsteroid(new Vector3f(0, 0, 30));

		inputManager.addListener(this, "autopilot", "reset", "drive", "rotateUp", "rotateDown", "rotateLeft",
				"rotateRight");
		inputManager.addMapping("drive", new KeyTrigger(Keyboard.KEY_SPACE));
		inputManager.addMapping("rotateUp", new KeyTrigger(Keyboard.KEY_UP));
		inputManager.addMapping("rotateDown", new KeyTrigger(Keyboard.KEY_DOWN));
		inputManager.addMapping("rotateLeft", new KeyTrigger(Keyboard.KEY_LEFT));
		inputManager.addMapping("rotateRight", new KeyTrigger(Keyboard.KEY_RIGHT));
		inputManager.addMapping("reset", new KeyTrigger(Keyboard.KEY_RETURN));
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
//		viewPort.setClearFlags(false, false, false);

		Vector3f lightDirection = new Vector3f(0, 0, -1);

		DirectionalLight light = new DirectionalLight();
//		light.setColor(new ColorRGBA(0.95f, 1.0f, 1.0f, 1f));
		light.setDirection(lightDirection);
		rootNode.addLight(light);

//		PssmShadowRenderer bsr = new PssmShadowRenderer(assetManager, 256, 3);
//		bsr.setDirection(lightDirection.normalizeLocal());
//		viewPort.addProcessor(bsr);

//		AmbientLight bgLight=new AmbientLight();
////		bgLight.setColor(new ColorRGBA(0.25f, 0.25f, 0.25f, 1f));
//		rootNode.addLight(bgLight);

		createHUD();

		autopilot = new Rotator();
		autopilot.setShip(sp);
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
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);

		Vector3f linearVelocity = sp.getLinearVelocity();
		linVeloText.setText(String.format("Linear Velocity: (%.2f, %.2f, %.2f)", linearVelocity.x, linearVelocity.y,
				linearVelocity.z));
		float[] angles = sp.getAngularVelocity().toAngles(null);
//		StringBuilder sb = new StringBuilder("(");
//		for (int i = 0; i < angles.length; i++) {
//			sb.append(angles[i] * FastMath.RAD_TO_DEG);
//			sb.append(", ");
//		}
//		sb.delete(sb.length() - 2, sb.length());
//		sb.append(')');

		angVeloText.setText(String.format("Angular Velocity: (%.2f, %.2f, %.2f)", angles[0], angles[1], angles[2]));

		Vector3f heading = sp.getRotation().mult(Vector3f.UNIT_X);
		headingText.setText(String.format("Heading: (%.2f, %.2f, %.2f)", heading.x, heading.y, heading.z));

		if (activateAP) {
			autopilot.update();
		}
	}

	@Override
	public void prePhysicsTick(PhysicsSpace space, float f) {
//
//		if (drive) {
//			physics.applyForce(physics.getPhysicsRotationMatrix().mult(new Vector3f(1, 0, 0)), physics
//					.getPhysicsRotationMatrix().mult(new Vector3f(-1, 0, 0)));
//
//		}
//		if (left) {
//
//			physics.applyForce(physics.getPhysicsRotationMatrix().mult(new Vector3f(0, 0, 1)), physics
//					.getPhysicsRotationMatrix().mult(new Vector3f(2, 0, 0)));
//			physics.applyForce(physics.getPhysicsRotationMatrix().mult(new Vector3f(0, 0, -1)), physics
//					.getPhysicsRotationMatrix().mult(new Vector3f(-2, 0, 0)));
//		}else if (right) {
//
//			physics.applyForce(physics.getPhysicsRotationMatrix().mult(new Vector3f(0, 0, -1)), physics
//					.getPhysicsRotationMatrix().mult(new Vector3f(2, 0, 0)));
//			physics.applyForce(physics.getPhysicsRotationMatrix().mult(new Vector3f(0, 0, 1)), physics
//					.getPhysicsRotationMatrix().mult(new Vector3f(-2, 0, 0)));
//		}
//		
//		if (up) {
//
//			physics.applyForce(physics.getPhysicsRotationMatrix().mult(new Vector3f(0, 1, 0)), physics
//					.getPhysicsRotationMatrix().mult(new Vector3f(2, 0, 0)));
//			physics.applyForce(physics.getPhysicsRotationMatrix().mult(new Vector3f(0, -1, 0)), physics
//					.getPhysicsRotationMatrix().mult(new Vector3f(-2, 0, 0)));
//		}else if (down) {
//
//			physics.applyForce(physics.getPhysicsRotationMatrix().mult(new Vector3f(0, -1, 0)), physics
//					.getPhysicsRotationMatrix().mult(new Vector3f(2, 0, 0)));
//			physics.applyForce(physics.getPhysicsRotationMatrix().mult(new Vector3f(0, 1, 0)), physics
//					.getPhysicsRotationMatrix().mult(new Vector3f(-2, 0, 0)));
//		}

//		if (next) {
//			physics.setAngularVelocity(new Vector3f(0, 0, 0.6f));
//		} else {
//			physics.setAngularVelocity(Vector3f.ZERO);
//			
//		}
	}

	@Override
	public void physicsTick(PhysicsSpace space, float f) {
//		System.out.println(physics.getAngularVelocity());
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("drive")) {
//			drive = isPressed;
			if (isPressed) {
				sp.getEngines().get(0).setCurrentForce(1f);
				sp.getEngines().get(1).setCurrentForce(1f);
			} else {
				sp.getEngines().get(0).setCurrentForce(0);
				sp.getEngines().get(1).setCurrentForce(0);
			}
//			if (isPressed) {
//				fire1.setParticlesPerSec(20);
//				fire2.setParticlesPerSec(20);
//			} else {
//				fire2.setParticlesPerSec(0);
//				fire1.setParticlesPerSec(0);
//			}
		} else if (name.equals("rotateUp")) {
			if (isPressed) {
				sp.getEngines().get(4).setCurrentForce(1f);
				sp.getEngines().get(9).setCurrentForce(1f);
			} else {
				sp.getEngines().get(4).setCurrentForce(0);
				sp.getEngines().get(9).setCurrentForce(0);
			}
//			up=isPressed;
//			Quaternion quat = new Quaternion();
//
//			if (isPressed) {
//				quat.fromAngleAxis(0.8f, physics.getPhysicsRotationMatrix().mult(Vector3f.UNIT_Z));
//
//			} else {
//				quat.fromAngleAxis(-0.8f, physics.getPhysicsRotationMatrix().mult(Vector3f.UNIT_Z));
//
//			}
//
//			float[] temp = new float[3];
//			quat.toAngles(temp);
//
//			physics.setAngularVelocity(physics.getAngularVelocity().addLocal(temp[0], temp[1], temp[2]));
		} else if (name.equals("rotateDown")) {
			if (isPressed) {
				sp.getEngines().get(5).setCurrentForce(1f);
				sp.getEngines().get(8).setCurrentForce(1f);
			} else {
				sp.getEngines().get(5).setCurrentForce(0);
				sp.getEngines().get(8).setCurrentForce(0);
			}
//			down=isPressed;
//			Quaternion quat = new Quaternion();
//
//			if (isPressed) {
//				quat.fromAngleAxis(-0.8f, physics.getPhysicsRotationMatrix().mult(Vector3f.UNIT_Z));
//
//			} else {
//				quat.fromAngleAxis(0.8f, physics.getPhysicsRotationMatrix().mult(Vector3f.UNIT_Z));
//
//			}
//
//			float[] temp = new float[3];
//			quat.toAngles(temp);
//
//			physics.setAngularVelocity(physics.getAngularVelocity().addLocal(temp[0], temp[1], temp[2]));
		} else if (name.equals("rotateLeft")) {
			if (isPressed) {
				sp.getEngines().get(2).setCurrentForce(1f);
				sp.getEngines().get(7).setCurrentForce(1f);
			} else {
				sp.getEngines().get(2).setCurrentForce(0);
				sp.getEngines().get(7).setCurrentForce(0);
			}
//			left=isPressed;
//			Quaternion quat = new Quaternion();
//
//			if (isPressed) {
//				quat.fromAngleAxis(0.8f, physics.getPhysicsRotationMatrix().mult(Vector3f.UNIT_Y));
//
//			} else {
//				quat.fromAngleAxis(-0.8f, physics.getPhysicsRotationMatrix().mult(Vector3f.UNIT_Y));
//
//			}
//
//			float[] temp = new float[3];
//			quat.toAngles(temp);
//
//			physics.setAngularVelocity(physics.getAngularVelocity().addLocal(temp[0], temp[1], temp[2]));
		} else if (name.equals("rotateRight")) {
			if (isPressed) {
				sp.getEngines().get(3).setCurrentForce(1f);
				sp.getEngines().get(6).setCurrentForce(1f);
			} else {
				sp.getEngines().get(3).setCurrentForce(0);
				sp.getEngines().get(6).setCurrentForce(0);
			}
//			right=isPressed;
//			Quaternion quat = new Quaternion();
//
//			if (isPressed) {
//				quat.fromAngleAxis(-0.8f, physics.getPhysicsRotationMatrix().mult(Vector3f.UNIT_Y));
//
//			} else {
//				quat.fromAngleAxis(0.8f, physics.getPhysicsRotationMatrix().mult(Vector3f.UNIT_Y));
//
//			}
//
//			float[] temp = new float[3];
//			quat.toAngles(temp);
//
//			physics.setAngularVelocity(physics.getAngularVelocity().addLocal(temp[0], temp[1], temp[2]));
		} else if (name.equals("reset")) {
			sp.getPhysics().setAngularVelocity(Vector3f.ZERO);
			sp.getPhysics().setLinearVelocity(Vector3f.ZERO);

			sp.getPhysics().setPhysicsLocation(Vector3f.ZERO);
			sp.getPhysics().setPhysicsRotation(Quaternion.IDENTITY);
		} else if (name.equals("autopilot")) {
			if (isPressed) {
				activateAP = !activateAP;
				if (activateAP) {
					String input = JOptionPane.showInputDialog("Enter tatget vector or cancel for stabilize");
					if (input != null) {
						autopilot.queueTask(new Vector3f(0, 1, 0), null);
					}else{
						autopilot.clearTask();
					}
				} else {
					sp.stopAllEngines();
				}
			}
		}
	}

}
