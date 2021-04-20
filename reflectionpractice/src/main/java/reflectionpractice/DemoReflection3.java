package reflectionpractice;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class DemoReflection3 {
	public  DemoReflection3() { }
	public  DemoReflection3(String s, int b) { }
	
	public static void main(String[] args) {
		try {
				Class c = Class.forName("reflectionpractice.DemoReflection3");
				
				Constructor[] cs = c.getDeclaredConstructors();
		
				for (int i=0; i<cs.length; i++) {
					
					Constructor c1 = cs[i];
					System.out.println("name : "+ c1.getName());
					System.out.println("declare Class :" + c1.getDeclaringClass());
				
					Class[] gpt = c1.getParameterTypes();
					
					for(int j=0; j<gpt.length; j++) {
						System.out.println("Param: "+gpt[j]);
					}
					
				}
				
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
