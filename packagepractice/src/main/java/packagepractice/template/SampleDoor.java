package packagepractice.template;



public class SampleDoor extends Door {
	public SampleDoor() {
		super();
	}

	@Override
	  public void close() 
	    { doorStatus = DoorStatus.CLOSED; }

	  @Override
	  public void open() { doorStatus = DoorStatus.OPENED; }

	@Override
	public String toString() {
		return "SampleDoor [doorStatus=" + doorStatus + "]";
	}
	
	
}

