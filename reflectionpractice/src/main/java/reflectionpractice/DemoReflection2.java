package reflectionpractice;

import java.lang.reflect.Method;

public class DemoReflection2 {
	public int sum(int a, int b) throws NoSuchFieldException{
		return a+b;
	}
	
	public static void main(String[] args) {
		try {
			Class c = Class.forName("reflectionpractice.DemoReflection2");
			
			Method m[] = c.getDeclaredMethods();
			
			for(int i=0; i<m.length; i++) {
				Method m1 = m[i];
				System.out.println("name:"+m1.getName());
				System.out.println("declare class :"+ m1.getDeclaringClass());
				
				Class[] gpt = m1.getParameterTypes();
				
				for(int j=0; j<gpt.length; j++) {
					System.out.println("Param: "+gpt[i]);
				}
				
				Class ept [] = m1.getExceptionTypes();
				for(int z=0; z<ept.length;z++) {
					System.out.println("Exception: "+ept[z]);
				}
				System.out.println("RetrunType: "+m1.getReturnType());
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
