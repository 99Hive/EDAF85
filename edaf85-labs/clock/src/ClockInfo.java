import clock.io.Choice;
import clock.io.ClockOutput;

import java.util.concurrent.Semaphore;

public class ClockInfo {

	private int hours, minutes, seconds;
	private int alarmHours, alarmMinutes, alarmSeconds;
    private ClockOutput out;
    private boolean alarm = false;
    private Semaphore inputSemaphore = new Semaphore(1);

    public ClockInfo(ClockOutput out) {
        this.out = out;
    }

    public void setAlarmTime(int h, int m, int s) throws InterruptedException {
        inputSemaphore.acquire();
        alarmHours = h;
        alarmMinutes = m;
        alarmSeconds = s;
        inputSemaphore.release();
    }
    //increment the timer, flash the blinker
    public void incrementTime() throws InterruptedException {
        inputSemaphore.acquire(); 
     
        seconds++;
        if (seconds >= 60) {
            seconds = 0;
            minutes++;
            if (minutes >= 60) {
                minutes = 0;
                hours++;
                if (hours >= 24) {
                    hours = 0;
                }
            }
        }

        out.displayTime(hours, minutes, seconds);
        inputSemaphore.release(); // Release the semaphore after the critical section
        if (alarm && isAlarmTime()) {
            // Start a new thread for alarm blinking
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime < 20000) { // Run for 20 seconds
                        alarmBlink();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
    
    //user choices
    public void handleUserInput(Choice choice, int h, int m, int s) throws InterruptedException {
        switch (choice) {
            case SET_TIME:
                setTime(h, m, s);
                break;
            case SET_ALARM:
                setAlarmTime(h, m, s);
                break;
            case TOGGLE_ALARM:
                toggleAlarm();
                break;
        }
    }

    public void setTime(int h, int m, int s) throws InterruptedException {
    	inputSemaphore.acquire();
        hours = h;
        minutes = m;
        seconds = s;
        inputSemaphore.release();
    }
    //alarm on/off
    public void toggleAlarm() /**throws InterruptedException**/ {
//    	inputSemaphore.acquire();
        alarm = !alarm;
        out.setAlarmIndicator(alarm);   
//        inputSemaphore.release();
    }

    public boolean isAlarmTime() {
        return alarmHours == hours && alarmMinutes == minutes && alarmSeconds == seconds;
    }

    public void alarmBlink() {
        out.alarm();
    }
}