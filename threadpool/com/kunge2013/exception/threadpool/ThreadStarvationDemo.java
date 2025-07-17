import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadStarvationDemo {

    public static void main(String[] args) {
        // 创建一个固定大小的线程池（这里故意设置很小的线程数）
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // 提交相互依赖的任务
        executor.submit(() -> {
            System.out.println("Task 1 started");
            // 任务1需要任务2的结果
            executor.submit(() -> {
                System.out.println("Sub-task for Task 1");
            }).get(); // 这里会阻塞等待子任务完成
            System.out.println("Task 1 finished");
            return null;
        });
        
        executor.submit(() -> {
            System.out.println("Task 2 started");
            // 任务2需要任务1的结果
            executor.submit(() -> {
                System.out.println("Sub-task for Task 2");
            }).get(); // 这里会阻塞等待子任务完成
            System.out.println("Task 2 finished");
            return null;
        });
        
        // 优雅关闭线程池
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}