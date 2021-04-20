package reflectionpractice;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DemoReflection5 {
	public int num = 5;
	
	public static void main(String[] args) {
		try {
				Class c = Class.forName("reflectionpractice.DemoReflection5");
				
				Field f = c.getField("num");
		
				DemoReflection5 demo = new DemoReflection5();	
				System.out.println( "num : " +demo.num);
				f.setInt(demo, 7);
				System.out.println( "num : " +demo.num);
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
