package spacegame.script;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;

public class LuaInputStream implements JavaFunction {

	private InputStream stream;
	
	private byte[] buffer;
	
	public LuaInputStream(InputStream stream) {
		this.stream = stream;
		
		buffer=new byte[1024];
	}

	@Override
	public int invoke(LuaState luaState) {
		try {
			byte[] buf=buffer;
			int nread= stream.read(buf);
			
			if(nread==-1){
				luaState.pushNil();
				return 1;
			}
			
			if(nread<buf.length){
				buf=Arrays.copyOf(buf, nread);
			}
			luaState.pushByteArray(buf);
			return 1;
		} catch (IOException e) {
			throw new LuaRuntimeException(e.getMessage(), e);
		}
	}

}