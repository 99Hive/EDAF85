package actor;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ActorThread<M> extends Thread {
	LinkedBlockingQueue<M> messageQueue= new LinkedBlockingQueue<>();
	
    /** Called by another thread, to send a message to this thread. */
    public void send(M message) {
        // TODO: implement this method (one or a few lines)
    	try {
			messageQueue.put(message);
		} catch (InterruptedException e) {
			System.out.println("interrupt");
		}
    	
    }
    
    /** Returns the first message in the queue, or blocks if none available. */
    protected M receive() throws InterruptedException {
        // TODO: implement this method (one or a few lines)
    	return messageQueue.take();
//        return null;
    }
    
    /** Returns the first message in the queue, or blocks up to 'timeout'
        milliseconds if none available. Returns null if no message is obtained
        within 'timeout' milliseconds. */
    protected M receiveWithTimeout(long timeout) throws InterruptedException {
        // TODO: implement this method (one or a few lines)
    	return messageQueue.poll(timeout, TimeUnit.MILLISECONDS);
//        return null;
    }
}