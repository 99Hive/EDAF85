package lift;

import java.lang.reflect.WildcardType;

public class liftThread extends Thread {

	private LiftView view; 
	private int nbrFloors; 
	private monitor monitor;
	
	public liftThread(LiftView view, monitor monitor) {
		this.view=view; 
		this.monitor = monitor;
	}
	
	
	public void run() {
		 
		int currentFloor = 0;
		boolean goUp = true;
		 
		while(!monitor.isEmpty()) {
			int toFloor;
			if (goUp) {
			    toFloor = currentFloor+1; // Going up
			} else {
			    toFloor = currentFloor - 1; // Going down
			}
			System.out.println("to floor: "+toFloor);
			
		       view.moveLift(currentFloor, toFloor);   // ride lift
		    	   		
		       monitor.currentfoor(toFloor);
				try {
					monitor.MoveLift(toFloor);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
		       if (goUp) {
	                currentFloor++;
	                if (currentFloor == 6) { // If at top floor, change direction
	                    goUp = false;
	                }
	            } else {
	                currentFloor--;
	                if (currentFloor == 0) { // If at bottom floor, change direction
	                    goUp = true;
	                }
	            }
		}
	}
	

	
	
	
}
