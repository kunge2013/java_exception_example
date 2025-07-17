import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadStarvationDeadlock {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Thread " + Thread.currentThread().getName() + " is running");
                }
            });
        }
    }
}
