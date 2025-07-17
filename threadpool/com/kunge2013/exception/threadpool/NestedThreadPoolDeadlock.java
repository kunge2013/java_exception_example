
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NestedThreadPoolDeadlock {
    // 使用固定大小的线程池，线程数较少
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception{
        // 外层任务
        Runnable outerTask = () -> {
            System.out.println("外层任务开始，由 " + Thread.currentThread().getName() + " 执行");
            
            try {
                // 内层任务
                Future<?> innerFuture = executor.submit(() -> {
                    System.out.println("内层任务开始，由 " + Thread.currentThread().getName() + " 执行");
                    try {
                        Thread.sleep(1000); // 模拟工作
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("内层任务结束");
                });
                Thread.sleep(10000L);
                // 外层任务等待内层任务完成
                innerFuture.get();
                System.out.println("外层任务结束");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // 提交两个外层任务
        executor.submit(outerTask);
        Thread.sleep(10000L);
        executor.submit(outerTask);
        
        // 不关闭线程池，让程序继续运行以观察死锁
        // executor.shutdown();
    }
}