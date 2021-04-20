package reflectionpractice;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class InvokeTest {

	public static void main(String[] args) throws Exception{
	    Class userClass = User.class;
	     
	    Constructor cs = userClass.getConstructor(new Class[]{String.class, String.class, String.class, Integer.class});
	     
	    User user = (User)cs.newInstance("joontID","joontPWD","joont",920000);
	     
	    Method method = userClass.getMethod("toString");
	     
	    System.out.println(method.invoke(user));
	}


}
