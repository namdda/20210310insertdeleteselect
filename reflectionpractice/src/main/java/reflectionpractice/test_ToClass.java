package reflectionpractice;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class test_ToClass{
	
	public static void printText(String t) {
		
		System.out.println("==================["+t+"]================");
		
		}
	public static void finish() {
		
		System.out.println("==================================");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		}
	
	public static void main(String[] args) {
		//접근 방법
	    Class<Book> bookClass = Book.class;

	    Book book = new Book();
	    Class<? extends Book> aClass = book.getClass();

	    try {
			Class<?> aClass1 = Class.forName("reflectionpractice.Book");
			
			printText("getFields");
		    Arrays.stream(bookClass.getFields()).forEach(System.out::println);
		    finish();

		    printText("getDeclaredFields");
		    Arrays.stream(bookClass.getDeclaredFields()).forEach(System.out::println);
		    finish();

		    printText("getMethods");
		    Arrays.stream(bookClass.getMethods()).forEach(System.out::println);
		    finish();

		    printText("getSuperclass");
		    System.out.println(Mybook.class.getSuperclass());
		    finish();

		    printText("getInterfaces");
		    Arrays.stream(Mybook.class.getInterfaces()).forEach(System.out::println);
		    finish();

		    printText("detail Info ");
		    Arrays.stream(bookClass.getDeclaredFields()).forEach(f -> {
		        printText("modifies");
		        int modifies = f.getModifiers();
		        System.out.println(f);
		        System.out.println(Modifier.isPrivate(modifies));
		        System.out.println(Modifier.isStatic(modifies));
		    });
		    finish();
		
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	   
	    
	}
    
}