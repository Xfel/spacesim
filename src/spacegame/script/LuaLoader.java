package spacegame.script;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoadException;
import com.jme3.asset.AssetLoader;
import com.naef.jnlua.LuaException;

public class LuaLoader implements AssetLoader {

	private static final Logger log = Logger.getLogger(LuaLoader.class.getName());

	@Override
	public Object load(AssetInfo assetInfo) throws IOException {
		LuaAppState las = LuaAppState.getInstance();

		if (las == null) {
			throw new AssetLoadException("We need an active LuaAppState for this loader to operate.");
		}

		InputStream is = assetInfo.openStream();
		try {
			las.loadConfigFile(is, assetInfo.getKey().getName());
		} catch (LuaException luaex) {
			throw new AssetLoadException("Error loading " + assetInfo.getKey().getName(), luaex);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				log.log(Level.WARNING, "Error closing stream", e);
			}

		}

		return null;
	}

}
