package wash.control;



import actor.ActorThread;
import wash.io.WashingIO;

public class WaterController extends ActorThread<WashingMessage> {
	WashingIO io; 
	private int machieneState = 0; //0 IDLE 1 FILL 2 DRAIN
	private double inputFlow = 0.1;
    private double outputFlow = 0.2;
	private ActorThread<WashingMessage> sender; 

    
    private boolean reachedLevel = false; 
    // TODO: add attributes
	

    public WaterController(WashingIO io) {
        this.io = io; 
    }
    

    @Override
    public void run() {
 
    		while(true) {
        		
        		try {
    				WashingMessage m =receiveWithTimeout(1000 / Settings.SPEEDUP);
    				
    				
    				if(m != null) {
        				 sender = m.sender();

    				switch(m.order()) {
    				case WATER_IDLE:
    					machieneState = 0; 
    					io.drain(false);
    					System.out.println("acc from IDLE ");
    					sender.send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
    				break; 
    				
    				case WATER_FILL:
    					
    					System.out.println("filling water \n");
    					machieneState = 1;
    					io.fill(true);
    					io.drain(false);
    					reachedLevel = false;

    					break; 
    					
    				case WATER_DRAIN:
    					System.out.println("draining water \n");
    					io.drain(true);
    					io.fill(false);
    					
    					reachedLevel = false;
    					machieneState = 2;     					
    				
    				break;
    				
    				default:
    					break;
    	    		}
        		}
    				checkwaterLevel();

    				
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} 
    	}
    }
   

	private void checkwaterLevel() throws InterruptedException {
		double level = io.getWaterLevel();

			if(machieneState == 1) {
				if(level >= 10-inputFlow) {
					io.fill(false);
					
					if(!reachedLevel) {
					sender.send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
					reachedLevel = true;
					}
				}
				
			} else if(machieneState == 2) {

				if(level < outputFlow) {
					io.drain(true);
					if(!reachedLevel) {
						sender.send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
						reachedLevel = true;
						}			
					}
				
			}
		
			
		
	}
    
}


	
	
	

