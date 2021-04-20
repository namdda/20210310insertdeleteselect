package reflectionpractice;

public class User {
    public String id;
    public String pwd;
    public String name;
    public Integer birthDate;
     
    public User(){}
 
    public User(String id, String pwd, String name, Integer birthDate) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.birthDate = birthDate;
    }
 
    @Override
    public String toString() {
        return "id : " + id + ", pwd : " + pwd + ", name : " + name +", birthdate : " + birthDate;
    }
     
    // setter , getter 생략
}


