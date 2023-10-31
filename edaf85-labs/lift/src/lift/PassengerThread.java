package lift;

import javax.swing.text.View;

public class PassengerThread extends Thread {
	 final int NBR_FLOORS = 7, MAX_PASSENGERS = 4;
     LiftView v; 
	
     monitor m;
     Passenger p;
	
	public PassengerThread(monitor m, LiftView v) {
		this.m=m;
		this.v=v;
		
	}
	public void run() {

		 p= v.createPassenger();
		
		int  fromFloor = p.getStartFloor();
		int toFloor = p.getDestinationFloor();
		
			p.begin(); 
			m.EnterLift(fromFloor, toFloor, p);
			p.enterLift();
			m.walking();
			
			m.ExitLift(fromFloor, toFloor, p);
			p.exitLift();
			m.walking();
	        p.end();                          // walk out (to the right)
	}
	
	public synchronized int getFloor() {
		return p.getDestinationFloor();
	}
}
