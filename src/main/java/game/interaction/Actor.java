package game.interaction;

import java.util.concurrent.atomic.AtomicReference;

public class Actor {

    private final Iterator iteration;
    private AtomicReference<Boolean> stop = new AtomicReference<>(true);
    private Thread thread;

    public Actor(Iterator iteration) {
        this.iteration = iteration;
    }

    public void run() {
        if (this.stop.get()) {
            stop.set(false);
            Thread parentThread = Thread.currentThread();
            this.thread = new Thread(() -> {
                while (!stop.get() && parentThread.isAlive()) {
                    iteration.iterate();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                thread.interrupt();
            });
            this.thread.start();
        }
    }

    public void stop() {
        this.stop.set(true);
    }

}
