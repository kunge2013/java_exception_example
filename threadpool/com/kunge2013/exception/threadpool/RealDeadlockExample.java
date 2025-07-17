
import java.util.concurrent.*;

public class RealDeadlockExample {
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    
    public static void main(String[] args) {
        // 提交3个任务，每个都会再提交子任务
        for (int i = 0; i < 3; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("父任务"+taskId+"开始，线程:"+Thread.currentThread().getName());
                
                Future<?> child = executor.submit(() -> {
                    System.out.println("子任务"+taskId+"开始，线程:"+Thread.currentThread().getName());
                    return null;
                });
                
                child.get(); // 等待子任务完成
                System.out.println("父任务"+taskId+"结束");
                return null;
            });
        }
    }
}