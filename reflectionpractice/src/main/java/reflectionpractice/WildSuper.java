package reflectionpractice;

import java.util.*;

//Person2 클래스
class Person2 {
 String name;

 // 기본 생성자
 Person2() {
 }
	
 // 생성자 오버로딩
 Person2(String name) {
     this.name = name;
 }

 public String toString() {
     return name;
 }
}

//Person2 상속 Man2 클래스
class Man2 extends Person2 {
	// 생성자
	 Man2(String name) {
	     this.name = name;
	 }

	 // name 반환 메소드
	 public String toString() {
	     return name.toString();
	 }
}

//Person2 상속 WoMan2 클래스
class WoMan2 extends Person2 {
	WoMan2(String name) {
	     this.name = name;
	 }

	 public String toString() {
	     return name.toString();
	 }
}

public class WildSuper {

 public static void main(String[] args) {

     // Person2
     List<Person2> listP = new ArrayList<Person2>();
     listP.add(new Person2("사람"));
     listP.add(new Person2("인간"));
     printData(listP); // 사람 인간

     // Man2
     List<Man2> listM = new ArrayList<Man2>();
     listM.add(new Man2("하현우"));
     listM.add(new Man2("박효신"));
     printData(listM); // 하현우 박효신

     // WoMan2
     List<WoMan2> listW = new ArrayList<WoMan2>();
     listW.add(new WoMan2("백예린"));
     listW.add(new WoMan2("박정현"));
//   printData(listW); → Man2 클래스의 상위 클래스가 아니기 때문에 메소드 호출 불가

 }

 // Man2 클래스와 그 상위 클래스로 생성된 인스턴스만 매개변수로 전달 가능
 public static void printData(List<? super Man2> list) {
     for (Object obj : list) {
         System.out.println(obj);
     }
 }

}
