package dev.omatheusmesmo.vintelli.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

class UiWorkQueueTest {

    @Test
    void queuedWorkWakesTheRenderLoopAndRunsWhenDrained() {
        UiWorkQueue queue = new UiWorkQueue();
        AtomicInteger wakeups = new AtomicInteger();
        AtomicInteger updates = new AtomicInteger();
        queue.wakeWith(wakeups::incrementAndGet);

        queue.execute(updates::incrementAndGet);
        queue.execute(updates::incrementAndGet);

        assertEquals(2, wakeups.get());
        assertEquals(0, updates.get());
        queue.drain();
        assertEquals(2, updates.get());
    }
}
