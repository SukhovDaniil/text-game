import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Multi {

    public static void main(String[] args) throws InterruptedException {
        ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(2);
        ScheduledFuture<?> feature;
        feature = executorService.scheduleWithFixedDelay(() -> {
                for (int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.printf("[%s] tuk %s%n", Thread.currentThread(), LocalDateTime.now());
                }
            },
            0, 1,
            TimeUnit.SECONDS);
        System.out.println("stop");
    }
}
