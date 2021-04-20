package packagepractice.template2;

public abstract class Door {
	protected DoorStatus doorStatus;

	public Door() {
		doorStatus = DoorStatus.CLOSED;
	}

	public DoorStatus getDoorStatus() {
		return doorStatus;
	}

	abstract public void close();

	abstract public void open();
}