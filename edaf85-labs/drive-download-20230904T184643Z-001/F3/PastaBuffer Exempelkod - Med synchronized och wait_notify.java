import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class pataWithBuffer {

    public static void main(String args[]) throws InterruptedException, IOException, IllegalThreadStateException {
        Buffer buffer = new Buffer();

        Thread cookThread = new Thread(() -> {
            try {
                while (true) {
                    BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
                    String readLine = read.readLine();
                    buffer.cookPasta(readLine);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread buyThread = new Thread(() -> {
            while (true) {
                try {
                    buffer.buyPasta();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        buyThread.start();
        cookThread.start();
    }

    static class Buffer {

        boolean bufferFull;
        boolean bufferEmpty;
        LinkedList<String> bufferList;

        public Buffer() {
            bufferFull = false;
            bufferEmpty = true;
            bufferList = new LinkedList<String>();
        }

        public synchronized void cookPasta(String readLine) throws InterruptedException, IOException {
            while (bufferFull || readLine.isEmpty())
                wait();

            if (readLine.equals("pasta")) {
                bufferList.add("pasta");
                bufferEmpty = false;
                if (bufferList.size() >= 3)
                    bufferFull = true;
            } else {
                System.out.format("Could not read %s\n", readLine);
            }
            notifyAll();
        }

        public synchronized void buyPasta() throws InterruptedException {
            while (bufferEmpty)
                wait();

            System.out.format("%s delivered, amount left: %d\n", bufferList.pollLast(), bufferList.size());
            bufferFull = false;

            if (bufferList.size() <= 0)
                bufferEmpty = true;

            notifyAll();
        }
    }
}