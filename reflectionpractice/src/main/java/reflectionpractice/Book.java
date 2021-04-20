package reflectionpractice;

public class Book {

    private String a;

    private static String B = "BOOK";

    private static final String C = "BOOK";

    public String d = "d";

    protected String e = "e";

    public Book() {}
    
    public Book(String a, String d, String e){
        this.a = a;
        this.d = d;
        this.e = e;
    }

    private void f(){
        System.out.println("F");
    }

    public void g(){
        System.out.println("g");
    }

    public int h(){
        return 100;
    }

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public static String getB() {
		return B;
	}

	public static void setB(String b) {
		B = b;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public static String getC() {
		return C;
	}

	@Override
	public String toString() {
		return "Book [a=" + a + ", d=" + d + ", e=" + e + "]";
	}

    
    
}



