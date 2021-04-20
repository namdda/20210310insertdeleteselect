package packagepractice.template;

public class main {

	public static void main(String[] args) {
		Door door = new SampleDoor();
		HyundaiMotor hyundaiMotor = new HyundaiMotor(door);
		hyundaiMotor.move(Direction.UP); // 위로 올라가도록 엘리베이터 제어
		
		System.out.println(hyundaiMotor);
	}
	
}
