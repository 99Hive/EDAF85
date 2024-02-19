import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class pata {

    public static void main(String args[]) {

        LinkedList<String> availablePasta = new LinkedList<String>();

        Semaphore available = new Semaphore(0);
        Semaphore mutex = new Semaphore(1);

        Thread cookPasta = new Thread(() -> {
            BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                try {
                    String readLine = read.readLine();
                    if (!readLine.isEmpty()) {
                        mutex.acquire();
                        if (readLine.equals("pasta")) {
                            availablePasta.add("pasta");
                            available.release();
                        } else {
                            System.out.format("Expected \"pasta\", but was \"%s\"\n",
                                    readLine);
                        }
                        mutex.release();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread consumePasta = new Thread(() -> {
            while (true) {
                try {
                    available.acquire();
                    mutex.acquire();
                    System.out.format("Sold pasta: %s,\namount pasta left: %d\n",
                            availablePasta.pollLast(),
                            availablePasta.size());
                    mutex.release();
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        cookPasta.start();
        consumePasta.start();
    }
}
