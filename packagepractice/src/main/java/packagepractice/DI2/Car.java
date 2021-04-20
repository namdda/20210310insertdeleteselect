package packagepractice.DI2;

public class Car {
    private CarMoveBehavior carMoveBehavior;
    
    public Car(CarMoveBehavior carMoveBehavior) {
        this.carMoveBehavior = carMoveBehavior;
    }
    
    public void move(){
        carMoveBehavior.action();
    }
    
    public void setMoveBehavior(CarMoveBehavior carMoveBehavior)
    {
        this.carMoveBehavior = carMoveBehavior;
    }
}