package game.interaction;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Actor {

    private final ScheduledExecutorService executorService;
    private final Runnable iteration;
    private Future<?> feature;

    public Actor(Runnable iteration, ScheduledExecutorService executorService) {
        this.iteration = iteration;
        this.executorService = executorService;
    }

    public void run() {
        log.debug("run from thread: {}", Thread.currentThread());
        if (this.feature == null || this.feature.isCancelled()) {
            this.feature = executorService.scheduleWithFixedDelay(iteration, 0, 1, TimeUnit.SECONDS);
        }
    }

    public void stop() {
        if (this.feature == null) {
            return;
        }
        this.feature.cancel(true);
    }

}
