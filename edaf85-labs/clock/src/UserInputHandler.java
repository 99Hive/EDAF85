import java.util.concurrent.Semaphore;

import clock.io.Choice;
import clock.io.ClockInput;
import clock.io.ClockInput.UserInput;

public class UserInputHandler implements Runnable {
    private ClockInput in;
    private ClockInfo clockInfo;

    public UserInputHandler(ClockInput in, ClockInfo clockInfo) {
        this.in = in;
        this.clockInfo = clockInfo;
    }

    @Override
    public void run() {
        try {
            while (true) {

                Semaphore inputSemaphore = in.getSemaphore();
                inputSemaphore.acquire();

                UserInput userInput = in.getUserInput();
                Choice choice = userInput.choice();

                int h = userInput.hours();
                int m = userInput.minutes();
                int s = userInput.seconds();

//                System.out.println("choice= " + choice + " h=" + h + " m=" + m + " s=" + s);

                clockInfo.handleUserInput(choice, h, m, s);

                // short delay to reduce CPU usage
//                Thread.sleep(100);
//

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
