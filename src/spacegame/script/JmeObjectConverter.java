package spacegame.script;

import java.lang.reflect.Method;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.naef.jnlua.Converter;
import com.naef.jnlua.DefaultConverter;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaType;

public class JmeObjectConverter implements Converter{

	@Override
	public int getTypeDistance(LuaState luaState, int index, Class<?> formalType) {
		// TODO Auto-generated method stub
		return DefaultConverter.getInstance().getTypeDistance(luaState, index, formalType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T convertLuaValue(LuaState luaState, int index, Class<T> formalType) {
//		if(formalType.isInterface() && luaState.type(index)==LuaType.FUNCTION && formalType.getDeclaredMethods().length==1){
//			if(formalType==JavaFunction.class){
//				if(luaState.isJavaFunction(index)){
//					return (T) luaState.toJavaFunction(index);
//				}
//			}else{
//				Method method= formalType.getDeclaredMethods()[0];
//			luaState.getProxy(index, interfaze)
//			}
//		}
		
		if(formalType.isAssignableFrom(Vector3f.class) && luaState.type(index)==LuaType.TABLE){
			
			luaState.getField(index, "x");
			float x=(float) luaState.toNumber(-1);
			luaState.pop(1);

			luaState.getField(index, "y");
			float y=(float) luaState.toNumber(-1);
			luaState.pop(1);

			luaState.getField(index, "z");
			float z=(float) luaState.toNumber(-1);
			luaState.pop(1);

			return (T) new Vector3f(x, y, z);
		}else if(formalType.isAssignableFrom(Quaternion.class) && luaState.type(index)==LuaType.TABLE){
			
			luaState.getField(index, "x");
			float x=(float) luaState.toNumber(-1);
			luaState.pop(1);

			luaState.getField(index, "y");
			float y=(float) luaState.toNumber(-1);
			luaState.pop(1);

			luaState.getField(index, "z");
			float z=(float) luaState.toNumber(-1);
			luaState.pop(1);

			luaState.getField(index, "w");
			float w=(float) luaState.toNumber(-1);
			luaState.pop(1);

			return (T) new Quaternion(x, y, z, w);
		}
		return DefaultConverter.getInstance().convertLuaValue(luaState, index, formalType);
	}

	@Override
	public void convertJavaObject(LuaState luaState, Object object) {
		if(object instanceof Vector3f){
			// call vector3:new(object)
			luaState.getGlobal("vector3");
			luaState.getField(-1, "new");
			luaState.insert(-2); // or luaState.remove(-2) wenn new keine methode
			
			luaState.pushJavaObjectRaw(object);
			luaState.call(2, 1);
			
			
			return;
		}
		
		DefaultConverter.getInstance().convertJavaObject(luaState, object);
	}

}
