package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

import static wash.control.WashingMessage.Order.*;


public class WashingProgram2 extends ActorThread<WashingMessage> {

    private WashingIO io;
    private ActorThread<WashingMessage> temp;
    private ActorThread<WashingMessage> water;
    private ActorThread<WashingMessage> spin;

    public WashingProgram2(WashingIO io,
                           ActorThread<WashingMessage> temp,
                           ActorThread<WashingMessage> water,
                           ActorThread<WashingMessage> spin) {
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

            // Fill the machine with water for pre-wash
            water.send(new WashingMessage(this, WATER_FILL));
            WashingMessage fillAck1 = receive();
            System.out.println("The water level is ok");

            // Start pre-wash: Set temperature to 40째C
            temp.send(new WashingMessage(this, TEMP_SET_40));
            WashingMessage tempAck1 = receive();
            System.out.println("The tempruture is 40");

            // Start pre-wash spinning
            spin.send(new WashingMessage(this, SPIN_SLOW));
            WashingMessage spinAck1 = receive();

            // Wait for 20 minutes for the pre-wash
            Thread.sleep((20 * 60000) / Settings.SPEEDUP);

            // Stop pre-wash spinning
            spin.send(new WashingMessage(this, SPIN_OFF));
            WashingMessage spinAck2 = receive();

            // Stop filling water for pre-wash
            water.send(new WashingMessage(this, WATER_IDLE));
            WashingMessage fillAck2 = receive();

            // Drain the water after pre-wash
            water.send(new WashingMessage(this, WATER_DRAIN));
            WashingMessage drainAck1 = receive();

            temp.send(new WashingMessage(this, TEMP_IDLE));
            WashingMessage ack5 = receive();
            System.out.println("Temp is down");

            water.send(new WashingMessage(this, WATER_DRAIN));
            WashingMessage ack6 = receive();

            water.send(new WashingMessage(this, WATER_IDLE));
            WashingMessage ack14 = receive();

            // Replace water with new, clean water
            water.send(new WashingMessage(this, WATER_FILL));
            WashingMessage fillAck3 = receive();

            // Start main wash: Set temperature to 60째C
            temp.send(new WashingMessage(this, TEMP_SET_60));
            WashingMessage tempAck2 = receive();
            System.out.println("Temp is 60 now");

            // Spin for 30 minutes during the main wash
            spin.send(new WashingMessage(this, SPIN_SLOW));
            WashingMessage ack1 = receive();
            System.out.println("Spinning slowly for 30 minutes");
            Thread.sleep((30 * 60000) / Settings.SPEEDUP);

            // Stop the spin
            temp.send(new WashingMessage(this, TEMP_IDLE));
        	WashingMessage ack15 = receive();
            System.out.println("Spinning stopped");

            water.send(new WashingMessage(this, WATER_DRAIN));
            WashingMessage ack16 = receive();

            water.send(new WashingMessage(this, WATER_IDLE));
            WashingMessage ack17 = receive();

            // Perform the main wash cycle 5 times
            for (int i = 0; i < 5; i++) {
                water.send(new WashingMessage(this, WATER_FILL));
                WashingMessage ack7 = receive();
                System.out.println("Filling water for main wash");

                spin.send(new WashingMessage(this, SPIN_SLOW));
                WashingMessage ack8 = receive();
                System.out.println("Spinning slowly for main wash");
                Thread.sleep((2 * 60000) / Settings.SPEEDUP);

                water.send(new WashingMessage(this, WATER_DRAIN));
                WashingMessage ack9 = receive();

                water.send(new WashingMessage(this, WATER_IDLE));
                WashingMessage ack12 = receive();
            }

            // Drain the water after the main wash
            water.send(new WashingMessage(this, WATER_DRAIN));
            WashingMessage drainAck2 = receive();

            // Spin for five simulated minutes (one minute == 60000 milliseconds)
            spin.send(new WashingMessage(this, SPIN_FAST));
            WashingMessage spinAck3 = receive();
            System.out.println("Spinning fast for 5 minutes");
            Thread.sleep((5 * 60000) / Settings.SPEEDUP);

            // Stop the spin
            spin.send(new WashingMessage(this, SPIN_OFF));
            WashingMessage spinAck4 = receive();
            System.out.println("Spinning stopped");

            // Now that the barrel has stopped, it is safe to unlock the hatch
            io.lock(false);
            System.out.println("Hatch unlocked");

        } catch (InterruptedException e) {
            // If the program was interrupted, set all controllers to idle
            temp.send(new WashingMessage(this, TEMP_IDLE));
            water.send(new WashingMessage(this, WATER_IDLE));
            spin.send(new WashingMessage(this, SPIN_OFF));
            System.out.println("Washing program terminated");
        }
    }
}