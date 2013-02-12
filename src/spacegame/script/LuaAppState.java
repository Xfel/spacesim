package spacegame.script;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

public class LuaAppState extends AbstractAppState {

	private static class LoadTextFile implements NamedJavaFunction {

		private static final Logger log = Logger.getLogger(LoadTextFile.class.getName());

		private AssetManager assetManager;

		public LoadTextFile(AssetManager assetManager) {
			this.assetManager = assetManager;
		}

		@Override
		public int invoke(LuaState luaState) {
			String name = luaState.checkString(1);
			luaState.pop(1);

			AssetInfo inf = assetManager.locateAsset(new AssetKey<Object>(name));

			if (inf == null) {
				luaState.pushBoolean(false);
				luaState.pushString("File not found");
				return 2;
			}
			InputStream is = inf.openStream();
			try {
				String code = readAll(is);

				luaState.pushBoolean(true);
				luaState.pushString(code);
				return 2;
			} catch (IOException e) {
				luaState.pushBoolean(false);
				luaState.pushString(e.getMessage());
				return 2;
			}
			finally {
				try {
					is.close();
				} catch (IOException e) {
					log.log(Level.WARNING, "Error closing stream", e);
				}
			}
		}

		@Override
		public String getName() {
			return "loadTextFile";
		}

	}
	
	private static LuaAppState instance;
	
	public static LuaAppState getInstance() {
		return instance;
	}

	public static String readAll(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();

		InputStreamReader isr = new InputStreamReader(is, "ASCII");

		char[] cbuf = new char[1024];

		while (true) {
			int read = isr.read(cbuf);
			if (read == -1) {
				break;
			}
			sb.append(cbuf, 0, read);
		}

		return sb.toString();
	}

	private LuaState luaState;
	private Application app;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.state.AbstractAppState#initialize(com.jme3.app.state.
	 * AppStateManager, com.jme3.app.Application)
	 */
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		if (initialized) {
			return;
		}
		initialized=true;
		
		this.app=app;

		// initialize lua state
		luaState = new LuaState();

		luaState.openLib(LuaState.Library.BASE);
		luaState.openLib(LuaState.Library.TABLE);
		luaState.openLib(LuaState.Library.STRING);
		luaState.openLib(LuaState.Library.MATH);
		luaState.openLib(LuaState.Library.COROUTINE);
		luaState.openLib(LuaState.Library.BIT32);
		// don't open os, io, java, debug and package

		// register native libs
		luaState.register(new LoadTextFile(app.getAssetManager()));

		// load own libs
		try {
			luaState.load(getClass().getResourceAsStream("/Scripts/general.lua"), "general.lua", "bt");
			luaState.call(0, 0);
		} catch (IOException e) {
		}


		instance=this;
		app.getAssetManager().registerLoader(LuaLoader.class, "lua");
//		app.getAssetManager().registerLoader(LuaLoader.class, "lua");

	}

	public LuaState getLuaState() {
		return luaState;
	}

	public void loadConfigFile(InputStream data, String name) throws IOException {
		String code = readAll(data);
		
		luaState.getGlobal("loadConfig");
		luaState.pushString(code);
		luaState.pushString(name);
		
		luaState.call(2, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.state.AbstractAppState#cleanup()
	 */
	@Override
	public void cleanup() {
		if (!initialized)
			return;
		app.getAssetManager().unregisterLoader(LuaLoader.class);
		instance=null;

		luaState.close();

		luaState = null;

		initialized = false;
	}

}
