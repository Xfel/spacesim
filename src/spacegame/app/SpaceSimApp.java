package spacegame.app;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.system.AppSettings;

public class SpaceSimApp extends SimpleApplication{

	private static SpaceSimApp instance;
	
	public static SpaceSimApp getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		AppSettings settings=new AppSettings(true);
		settings.setTitle("SpaceSim");
		
		instance=new SpaceSimApp();
		instance.setSettings(settings);
		instance.start();
	}
	
	@Override
	public void simpleInitApp() {
		// TODO Auto-generated method stub
		
	}

}
