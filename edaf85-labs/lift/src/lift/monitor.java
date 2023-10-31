package lift;


public class monitor{
	int MaxPassengers=4;
	LiftView lv;
	
	private int nbrPass=0;
	private int walking;
	private int currentF;
	private int inside;
	private boolean moving=false;
	private int[] toEnter = new int[7];
	private int[] toExit = new int[7];
    monitor m;
	PassengerThread pt=new PassengerThread(m, lv);
	
	private Boolean doorsOpen = false; 

	
	public monitor(LiftView lv,int MaxPassengers) {
		this.lv=lv;
		this.MaxPassengers=MaxPassengers;
		this.inside=0;
		this.currentF=0;
		this.walking=0;
	}
	
	public synchronized void MoveLift(int from) throws InterruptedException {
//		lv.showDebugInfo(toEnter, toExit);
		moving =true;
		while(toExit[from]>0 || (toEnter[from]>0 && inside<4 || walking>0)) {
			
			if(!doorsOpen) {
				lv.openDoors(from);
				doorsOpen=true;
				
				notifyAll();
			}
			moving =false;
			wait();			
//			break;
		}
		if(doorsOpen) {
			lv.closeDoors();
			doorsOpen= false;
		}
		notifyAll();
		moving=true;
		

//		lv.showDebugInfo(toEnter, toExit);

	}

	public synchronized void EnterLift(int from, int to, Passenger p) {
		toEnter[from]++;
//		lv.showDebugInfo(toEnter, toExit);
		while(!(currentF == from && inside<4 && moving==false)){
			try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }	
		}
		toExit[to]++;
		toEnter[from]--;
		inside++;
		walking++;
		notifyAll();
//		lv.showDebugInfo(toEnter, toExit);
	}
	
	public synchronized void ExitLift(int from, int to, Passenger p) {
		while(!(currentF == to && doorsOpen)){
			try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }	
		}
		toExit[to]--;
		inside--;
		walking++;
		nbrPass++;
		notifyAll();
//		System.out.println("walking1 "+ nbrPass);
//		System.out.println("walking2 "+ walking);

//		lv.showDebugInfo(toEnter, toExit);
	}
	
	
	public synchronized void currentfoor(int floor) {
//		moving =false;
		currentF = floor;
		notifyAll();
	}
	
	public synchronized void walking() {
		walking--;
		notifyAll();
	}
	
	public boolean isEmpty() {
		if(nbrPass>=21) {
			return true;
		}
		return false;
	}
	


	
}
