package spacegame.util;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.renderer.Camera;

public class NumpadFlyByCamera extends FlyByCamera{

	private static String[] mappings = new String[]{
        "FLYCAM_Left",
        "FLYCAM_Right",
        "FLYCAM_Up",
        "FLYCAM_Down",

        "FLYCAM_StrafeLeft",
        "FLYCAM_StrafeRight",
        "FLYCAM_Forward",
        "FLYCAM_Backward",

        "FLYCAM_ZoomIn",
        "FLYCAM_ZoomOut",
        "FLYCAM_RotateDrag",

        "FLYCAM_Rise",
        "FLYCAM_Lower",
        
        "FLYCAM_InvertY"
    };
	
	public NumpadFlyByCamera(Camera cam) {
		super(cam);
	}

	@Override
	public void registerWithInput(InputManager inputManager) {
		this.inputManager = inputManager;
        
        // both mouse and button - rotation of cam
        inputManager.addMapping("FLYCAM_Left", new MouseAxisTrigger(MouseInput.AXIS_X, true));

        inputManager.addMapping("FLYCAM_Right", new MouseAxisTrigger(MouseInput.AXIS_X, false));

        inputManager.addMapping("FLYCAM_Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false));

        inputManager.addMapping("FLYCAM_Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true));

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("FLYCAM_ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("FLYCAM_ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_NUMPAD4));
        inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_NUMPAD6));
        inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_NUMPAD8));
        inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(KeyInput.KEY_NUMPAD2));
        inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_NUMPAD9));
        inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_NUMPAD3));

        inputManager.addListener(this, mappings);
        inputManager.setCursorVisible(dragToRotate || !isEnabled());

        Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks != null && joysticks.length > 0){
            for (Joystick j : joysticks) {
                mapJoystick(j);
            }
        }
	}
	
}
