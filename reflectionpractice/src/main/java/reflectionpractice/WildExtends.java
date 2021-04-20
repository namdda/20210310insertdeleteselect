package reflectionpractice;

import java.util.*;

//Person 상속X 클래스
class Test {
 String name;

}

//Person 클래스
class Person {
 String name;

}

//Person 상속 Man 클래스
class Man extends Person {
 // 생성자
 Man(String name) {
     this.name = name;
 }

 // name 반환 메소드
 public String toString() {
     return name.toString();
 }
}

//Person 상속 Woman 클래스
class Woman extends Person {
 Woman(String name) {
     this.name = name;
 }

 public String toString() {
     return name.toString();
 }
}

public class WildExtends {

 public static void main(String[] args) {

     // Person
     List<Person> listP = new ArrayList<Person>();
     listP.add(new Person());
     printData(listP);        // j200210.Person@15db9742

     // Man
     List<Man> listM = new ArrayList<Man>();
     listM.add(new Man("이순신"));
     listM.add(new Man("하현우"));
     listM.add(new Man("박효신"));
     printData(listM);    // 이순신    하현우    박효신

     // Woman
     List<Woman> listW = new ArrayList<Woman>();
     listW.add(new Woman("유관순"));
     listW.add(new Woman("백예린"));
     listW.add(new Woman("박정현"));
     printData(listW);    // 유관순    백예린    박정현

     // Test
     List<Test> listT = new ArrayList<Test>();
     listT.add(new Test());
//   printData(listT); → Person 클래스를 상속받지 않았기 때문에 메소드 호출 불가

 }

 // Person 클래스와 그 하위 클래스로 생성된 인스턴스만 매개변수로 전달 가능
 public static void printData(List<? extends Person> list) {
     for (Person obj : list) {
         System.out.println(obj);
     }
 }

}