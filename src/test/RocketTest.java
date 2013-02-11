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
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

public class RocketTest extends SimpleApplication implements PhysicsTickListener, ActionListener {

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);
		Logger.getLogger("spacesim").setLevel(Level.INFO);

		RocketTest app = new RocketTest();
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
	private BitmapText linVeloText;
	private BitmapText angVeloText;
	private BitmapText headingText;
	private BitmapText apText;
	
	private SimpleSpaceObject rocket;
	private Geometry forceArrow;

	@Override
	public void simpleInitApp() {
		physicsState = new BulletAppState();
		physicsState.setThreadingType(ThreadingType.PARALLEL);
		stateManager.attach(physicsState);
		// physicsState.getPhysicsSpace().enableDebug(assetManager);
		physicsState.getPhysicsSpace().setGravity(new Vector3f(0, 0, 0));
		physicsState.getPhysicsSpace().addTickListener(this);

		// viewPort.setBackgroundColor(ColorRGBA.Blue);

		flyCam.setEnabled(true);
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(10f);
		
		
		createRocket();
		 

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

	}

	

	private void createRocket() {
		Geometry geom=new Geometry("Rocket", new Cylinder(20, 20, 1, 2));
		geom.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
		
		forceArrow=new Geometry("ForceArrow", new Arrow(new Vector3f(1,0,0)));
		forceArrow.setMaterial(assetManager.loadMaterial("Common/Materials/WhiteColor.j3m"));
		forceArrow.setLocalTranslation(0, 0, 1);
		
		Node rnode=new Node();
		rnode.attachChild(geom);
		rnode.attachChild(forceArrow);
		
		rocket = new SimpleSpaceObject(rnode, 5);

		rootNode.attachChild(rocket.getNode());
//		astObj.getPhysics().setPhysicsLocation(vector3f);

		physicsState.getPhysicsSpace().add(rocket.getPhysics());
		physicsState.getPhysicsSpace().addTickListener(rocket);
		rocket.getPhysics().setLinearVelocity(new Vector3f(0, 0, 0));
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

		Vector3f linearVelocity = rocket.getLinearVelocity();
		linVeloText.setText(String.format("Linear Velocity: (%.2f, %.2f, %.2f)", linearVelocity.x, linearVelocity.y,
				linearVelocity.z));
		float[] angles = rocket.getAngularVelocity().toAngles(null);
		// StringBuilder sb = new StringBuilder("(");
		// for (int i = 0; i < angles.length; i++) {
		// sb.append(angles[i] * FastMath.RAD_TO_DEG);
		// sb.append(", ");
		// }
		// sb.delete(sb.length() - 2, sb.length());
		// sb.append(')');

		angVeloText.setText(String.format("Angular Velocity: (%.2f, %.2f, %.2f)", angles[0] * FastMath.RAD_TO_DEG,
				angles[1] * FastMath.RAD_TO_DEG, angles[2] * FastMath.RAD_TO_DEG));

		Vector3f heading = rocket.getRotation().multLocal(new Vector3f(0, 0, -1));
		headingText.setText(String.format("Heading: (%.2f, %.2f, %.2f)", heading.x, heading.y, heading.z));

		
		Vector3f rtarget=new Vector3f(20, 0, 0);
		float rforce=1f;
		
		Vector3f direction = rtarget.subtractLocal(rocket.getLocation());
		
		Vector3f localDH = direction.normalize();
		Quaternion rot = new Quaternion();
		rot.fromAngleAxis(heading.angleBetween(localDH)/2, heading.cross(localDH, localDH));
		
		Vector3f forceDir=rot.inverse().multLocal(new Vector3f(0,  0,-1));
		
		rocket.getPhysics().applyForce(forceDir.multLocal(rforce), rocket.getRotation().multLocal(new Vector3f(0,  0, 1)));
		
		((Arrow)forceArrow.getMesh()).setArrowExtent(forceDir.negate());
	}

	@Override
	public void prePhysicsTick(PhysicsSpace space, float f) {
		
	}

	@Override
	public void physicsTick(PhysicsSpace space, float f) {
		// System.out.println(physics.getAngularVelocity());
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		
	}

}
