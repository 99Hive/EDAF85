package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;
import wash.simulation.WashingSimulator;

public class Wash {

    public static void main(String[] args) throws InterruptedException {
        WashingSimulator sim = new WashingSimulator(Settings.SPEEDUP);

        WashingIO io = sim.startSimulation();

        TemperatureController temp = new TemperatureController(io);
        WaterController water = new WaterController(io);
        SpinController spin = new SpinController(io);

        temp.start();
        water.start();
        spin.start();

        ActorThread<WashingMessage> currentProgram = null; // Reference to the currently running program

        while (true) {
            int n = io.awaitButton();
            System.out.println("user selected program " + n);
            if (n == 0) {
                if (currentProgram != null) {
                    currentProgram.interrupt(); 
                    currentProgram = null; // Reset the current program reference
                }
            } else if (n == 1 && currentProgram == null) {
                currentProgram = new WashingProgram1(io, temp, water, spin);
                currentProgram.start();
            } else if (n == 2 && currentProgram == null) {
                currentProgram = new WashingProgram2(io, temp, water, spin);
                currentProgram.start();
            }
            else if (n == 3 && currentProgram == null) {
                currentProgram = new WashingProgram3(io, temp, water, spin);
                currentProgram.start();
            }
            
        }
    }
}