import clock.AlarmClockEmulator;
//import clock.io.Choice;
import clock.io.ClockInput;
//import clock.io.ClockInput.UserInput;
import clock.io.ClockOutput;


public class ClockMain {

    public static void main(String[] args) throws InterruptedException {
        AlarmClockEmulator emulator = new AlarmClockEmulator();

        ClockInput in = emulator.getInput();
        ClockOutput out = emulator.getOutput();

        ClockInfo clockInfo = new ClockInfo(out);

        Thread userInputThread = new Thread(new UserInputHandler(in, clockInfo));

        userInputThread.start();
        
        long t= System.currentTimeMillis();
        while (true) {
        	clockInfo.incrementTime();
        	t+=1000;
        	long sleepTimeMillis = t - System.currentTimeMillis();
        	if(sleepTimeMillis>0) Thread.sleep(sleepTimeMillis);
        	
        	

        }
    }
}