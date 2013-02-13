package spacegame.script;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import spacegame.model.modules.EngineModule;
import spacegame.model.modules.PowerplantModule;
import spacegame.model.structure.ShipFrame;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoadException;
import com.jme3.asset.AssetLoader;
import com.naef.jnlua.LuaException;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

public class LuaLoader implements AssetLoader {

	public static class ConfigNewInstance implements NamedJavaFunction {

		@Override
		public String getName() {
			return "newInstance";
		}

		@Override
		public int invoke(LuaState luaState) {
			String clsid = luaState.checkString(1);
			luaState.pop(1);

			luaState.pushJavaObjectRaw(createObject(clsid));
			return 1;
		}

	}

	private static final Logger log = Logger.getLogger(LuaLoader.class.getName());

	private static HashMap<String, Class<?>> aliases;

	static {
		aliases = new HashMap<String, Class<?>>();

		aliases.put("ShipFrame", ShipFrame.class);
		aliases.put("Engine", EngineModule.class);
		aliases.put("Powerplant", PowerplantModule.class);
	}

	private static Object createObject(String clsid) {
		try {
			Class<?> cls = aliases.get(clsid);

			if (cls == null) {
				cls = Class.forName(clsid);

			}

			return cls.newInstance();
		} catch (ClassNotFoundException e) {
			throw new LuaRuntimeException("Class not found: " + clsid);
		} catch (InstantiationException e) {
			throw new LuaRuntimeException("Invalid load class: " + clsid);
		} catch (IllegalAccessException e) {
			throw new LuaRuntimeException("Invalid load class: " + clsid);
		}
	}

	public static void initConfigLib(LuaState luaState) throws IOException {
		luaState.register("config", new NamedJavaFunction[] { new ConfigNewInstance() }, true);

		for (Map.Entry<String, Class<?>> entry : aliases.entrySet()) {
			luaState.pushString(entry.getKey());
			luaState.pushValue(-1);
			luaState.setTable(-3);
		}

		luaState.pop(1);
		InputStream is = LuaLoader.class.getResourceAsStream("/Scripts/loader.lua");
		try {
			luaState.load(is, "loader", "bt");
			luaState.call(0, 0);
		} finally {
			is.close();
		}
	}

	@Override
	public Object load(AssetInfo assetInfo) throws IOException {
		LuaAppState las = LuaAppState.getInstance();

		if (las == null) {
			throw new AssetLoadException("We need an active LuaAppState for this loader to operate.");
		}

		LuaState luaState = las.getLuaState();

		InputStream is = assetInfo.openStream();
		try {

			luaState.getGlobal("loadConfig");
			luaState.pushJavaFunction(new LuaInputStream(is));
			luaState.pushString(LuaAppState.extractFileName(assetInfo.getKey().getName()));

			luaState.call(2, 1);

			Object result = luaState.toJavaObjectRaw(-1);
			luaState.pop(1);

			return result;
		} catch (LuaException luaex) {
			if (luaex instanceof LuaRuntimeException) {
				LuaRuntimeException lre = (LuaRuntimeException) luaex;
				lre.printLuaStackTrace();
			}
			throw new AssetLoadException("Error loading " + assetInfo.getKey().getName(), luaex);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				log.log(Level.WARNING, "Error closing stream", e);
			}

		}
	}

}
