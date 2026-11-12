package dev.omatheusmesmo.vintelli.tui;

import java.util.concurrent.ConcurrentLinkedQueue;

import jakarta.inject.Singleton;

/** Queues asynchronous provider results until the next shell render. */
@Singleton
public final class UiWorkQueue {

    private final ConcurrentLinkedQueue<Runnable> pending = new ConcurrentLinkedQueue<>();
    private volatile Runnable wakeup = () -> { };

    /** Wakes the render loop after an asynchronous update is queued. */
    public void wakeWith(Runnable wakeup) {
        this.wakeup = wakeup == null ? () -> { } : wakeup;
    }

    public void execute(Runnable update) {
        pending.add(update);
        wakeup.run();
    }

    void drain() {
        Runnable update;
        while ((update = pending.poll()) != null) {
            update.run();
        }
    }
}
