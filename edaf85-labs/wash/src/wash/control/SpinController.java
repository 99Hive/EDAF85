package wash.control;

import actor.ActorThread;
import wash.control.WashingMessage.Order;
import wash.io.WashingIO;
import wash.io.WashingIO.Spin;

public class SpinController extends ActorThread<WashingMessage> {
    private WashingIO io;
    private ActorThread<WashingMessage> sender;
    boolean spinDirectionToggle;
    private Spin direction = Spin.RIGHT;
    private int slow=1;
    private int normal=2;
    private int fast=3;
    private int state=2;

    
    public SpinController(WashingIO io) {
        this.io = io;
    }

    @Override
    public void run() {
        try {
            while (true) {
                WashingMessage m = receiveWithTimeout(40000 / Settings.SPEEDUP);

                if (m != null) {
                    System.out.println("got " + m);
                    sender = m.sender();

                    switch (m.order()) {
                        case SPIN_SLOW:
                        	state = slow;
                            System.out.println("spin ............." + direction);
                            sender.send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                            break;

                        case SPIN_FAST:
                        	state=fast;
                            io.setSpinMode(Spin.FAST);
                            sender.send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                            break;

                        case SPIN_OFF:
                        	state=normal;
                            io.setSpinMode(Spin.IDLE);
                            sender.send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                            break;
                        default:
                            break;
                    }
                }
                if(state==slow ) {
                	toggleSpinDirection(direction);
                }
                
            }
        } catch (InterruptedException unexpected) {
            throw new Error(unexpected);
        }
    }

    private void toggleSpinDirection(Spin currentDirection) throws InterruptedException {
        if (currentDirection == Spin.RIGHT && direction!=Spin.IDLE ) {
            io.setSpinMode(Spin.LEFT);
            direction = Spin.LEFT;
        } else if (currentDirection == Spin.LEFT && direction!=Spin.IDLE ) {
            io.setSpinMode(Spin.RIGHT);
            direction = Spin.RIGHT;
        }
        
    }
}
