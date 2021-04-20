package packagepractice.template;

public class HyundaiMotor {
	private Door door;
	private MotorStatus motorStatus;
	private Direction direction;

	public HyundaiMotor(Door door) {
		this.door = door;
		motorStatus = MotorStatus.STOPPED; // 초기: 멈춘 상태
	}

	private void moveHyundaiMotor(Direction direction) {
			motorStatus =MotorStatus.MOVING;
			this.direction = direction;
	}

	public MotorStatus getMotorStatus() {
		return motorStatus;
	}

	private void setMotorStatus(MotorStatus moving) {
		this.motorStatus = motorStatus;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	private void setDirection(Direction direction) {
		this.direction = direction;
		
	}
	

	/* 엘리베이터 제어 */
	public void move(Direction direction) {
		MotorStatus motorStatus = getMotorStatus();
		// 이미 이동 중이면 아무 작업을 하지 않음
		if (motorStatus == MotorStatus.MOVING)
			return;

		DoorStatus doorStatus = door.getDoorStatus();
		// 만약 문이 열려 있으면 우선 문을 닫음
		if (doorStatus == DoorStatus.OPENED)
			door.close();

		// Hyundai 모터를 주어진 방향으로 이동시킴
		moveHyundaiMotor(direction);
		// 모터 상태를 이동 중으로 변경함
		setMotorStatus(MotorStatus.MOVING);
		setDirection(direction);
		
	}

	@Override
	public String toString() {
		return "HyundaiMotor [door=" + door + ", motorStatus=" + motorStatus + ", direction=" + direction + "]";
	}

	
	
	
}
