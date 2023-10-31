
import java.lang.Thread;
import java.lang.reflect.Array;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
import javax.swing.text.View;

import lift.LiftView;
import lift.Passenger;
import lift.PassengerThread;
import lift.liftThread;
import lift.monitor;

public class MultPassengers {
	


	public static void main(String[] args) throws InterruptedException {
		PassengerThread[] pasvek= new PassengerThread[20];
	
		final int NBR_FLOORS = 7, MAX_PASSENGERS = 4;
		LiftView view = new LiftView(NBR_FLOORS, MAX_PASSENGERS);
		monitor m = new monitor(view, MAX_PASSENGERS);
		
		liftThread lt = new liftThread(view, m);
		PassengerThread pt = new PassengerThread(m, view);

		for(int i=0; i<pasvek.length; i++) {
			pasvek[i] = new PassengerThread(m, view);
		}
		for(PassengerThread p: pasvek) {
			p.start();
		}
		lt.start(); // Start the lift thread
		pt.start(); // Start the passenger thread
	}
	
	
	
}
