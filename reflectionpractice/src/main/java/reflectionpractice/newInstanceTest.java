package reflectionpractice;

import java.lang.reflect.Constructor;

public class newInstanceTest {
	
	public static void main(String[] args) throws Exception{
	    Class userClass = User.class;
	     
	    Constructor cs = userClass.getConstructor(new Class[]{String.class, String.class, String.class, Integer.class});
	     
	    User user = (User)cs.newInstance("joontID","joontPWD","joont",920000);
	    System.out.println(user.toString());
	}




}
