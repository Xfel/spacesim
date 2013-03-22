package spacegame.script;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaValueProxy;

public class LuaUpdateControl extends AbstractControl {

	private LuaValueProxy function;

	public LuaUpdateControl(LuaValueProxy function) {
		this.function = function;
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		LuaUpdateControl control = new LuaUpdateControl(function);
		control.setSpatial(spatial);
		return control;
	}

	@Override
	protected void controlUpdate(float tpf) {
		LuaState lua = function.getLuaState();

		function.pushValue();
		lua.pushNumber(tpf);

		lua.call(1, 0);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// do nothing
	}

	@Override
	public void read(JmeImporter im) throws IOException {
		super.read(im);

		byte[] fdata = im.getCapsule(this).readByteArray("function", null);

		if (fdata != null && LuaAppState.getInstance() != null) {
			LuaState lua = LuaAppState.getInstance().getLuaState();
			
			lua.load(new ByteArrayInputStream(fdata), null, "b");
			function=lua.getProxy(-1);
			lua.pop(1);
		}

	}

	@Override
	public void write(JmeExporter ex) throws IOException {
		super.write(ex);

		LuaState lua = function.getLuaState();
		function.pushValue();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		lua.dump(baos);
		lua.pop(1);

		ex.getCapsule(this).write(baos.toByteArray(), "function", null);
	}

}
