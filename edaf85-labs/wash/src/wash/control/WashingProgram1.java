package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

import static wash.control.WashingMessage.Order.*;

/**
 * Program 3 for washing machine. This also serves as an example of how washing
 * programs can be structured.
 * 
 * This short program stops all regulation of temperature and water levels,
 * stops the barrel from spinning, and drains the machine of water.
 * 
 * It can be used after an emergency stop (program 0) or a power failure.
 */
public class WashingProgram1 extends ActorThread<WashingMessage> {

    private WashingIO io;
    private ActorThread<WashingMessage> temp;
    private ActorThread<WashingMessage> water;
    private ActorThread<WashingMessage> spin;
    
    public WashingProgram1(WashingIO io,
                           ActorThread<WashingMessage> temp,
                           ActorThread<WashingMessage> water,
                           ActorThread<WashingMessage> spin) 
    {
        this.io = io;
        this.temp = temp;
        this.water = water;
        this.spin = spin;
    }
    
    @Override
    public void run() {
        try {
        	// Lock the hatch
        	
        	io.lock(true);
        	// Instruct SpinController to rotate barrel slowly, back and forth
        	// Expect an acknowledgment in response.
        	
        	//temp.send(new WashingMessage(this, TEMP_SET_40));
        	water.send(new WashingMessage(this, WATER_FILL));
        	WashingMessage ack3 = receive();
        	System.out.println("washing program 1 got " + ack3);

        	water.send(new WashingMessage(this, WATER_IDLE));
        	WashingMessage ack11 = receive();
        	System.out.println("washing program 1 got " + ack11);
        	
        	temp.send(new WashingMessage(this, TEMP_SET_40));
        	WashingMessage ack4 = receive();
        	System.out.println("washing program 1 got " + ack4);
        	//Thread.sleep(30 * 60000 / Settings.SPEEDUP);
        	

        	System.out.println("setting SPIN_SLOW...");

        	spin.send(new WashingMessage(this, SPIN_SLOW));
        	WashingMessage ack1 = receive();
        	System.out.println("washing program 1 got " + ack1); 
        	Thread.sleep((30  * 60000) / Settings.SPEEDUP);
            	
 
        	
        	// Spin for five simulated minutes (one minute == 60000 milliseconds)
        	
        	// Instruct SpinController to stop spin barrel spin.
        	// Expect an acknowledgment in response.
        	
        	
        	temp.send(new WashingMessage(this, TEMP_IDLE));
        	WashingMessage ack5 = receive();
        	System.out.println("washing program 1 got " + ack5);
        	
        	water.send(new WashingMessage(this, WATER_DRAIN));
        	WashingMessage ack6 = receive();
        	System.out.println("washing program 1 got " + ack6);
        	
        	water.send(new WashingMessage(this, WATER_IDLE));
        	WashingMessage ack14 = receive();
        	System.out.println("washing program 1 got " + ack14);
        	
        	for(int i = 0; i < 5 ; i++) {
        	water.send(new WashingMessage(this, WATER_FILL));
        	WashingMessage ack7 = receive();
        	System.out.println("washing program 1 got " + ack7);

        	System.out.println("setting SPIN_SLOW...");
        	spin.send(new WashingMessage(this, SPIN_SLOW));
        	WashingMessage ack8 = receive();
        	System.out.println("washing program 1 got " + ack8);
        	
        	Thread.sleep((2  * 60000) / Settings.SPEEDUP);

        	water.send(new WashingMessage(this, WATER_DRAIN));
        	WashingMessage ack9 = receive();
        	System.out.println("washing program 1 got " + ack9);
        	
        	water.send(new WashingMessage(this, WATER_IDLE));
        	WashingMessage ack12 = receive();
        	System.out.println("washing program 1 got " + ack12);

        	}
        	
        	
        	water.send(new WashingMessage(this, WATER_DRAIN));
        	WashingMessage ack17 = receive();
        	System.out.println("washing program 1 got " + ack17);
        	
        	
        	spin.send(new WashingMessage(this, SPIN_FAST));
        	WashingMessage ack13 = receive();
        	System.out.println("washing program 1 got " + ack13); 
        	Thread.sleep((5  * 60000) / Settings.SPEEDUP);
        	
        	spin.send(new WashingMessage(this, SPIN_OFF));
        	WashingMessage ack20 = receive();
        	System.out.println("washing program 1 got " + ack20);

        	// Now that the barrel has stopped, it is safe to open the hatch.
        	
        	
        	water.send(new WashingMessage(this, WATER_IDLE));
        	WashingMessage ack21 = receive();
        	System.out.println("washing program 1 got " + ack21);
        	io.lock(false);
        	
            System.out.println("hatch locked ");

        	
        } catch (InterruptedException e) {
            
            // If we end up here, it means the program was interrupt()'ed:
            // set all controllers to idle

            System.out.println("trying to interrupt");
			temp.send(new WashingMessage(this, TEMP_IDLE));
			water.send(new WashingMessage(this, WATER_IDLE));
			spin.send(new WashingMessage(this, SPIN_OFF));
            
            System.out.println("washing program terminated");
        }
        
        
    }
}
