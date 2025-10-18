package edu.polina.docscountingwords;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ManagedSleepBlocker implements ForkJoinPool.ManagedBlocker {
    boolean isSleepCompleted = false;
    long timeSleeping;

    public ManagedSleepBlocker(long timeSleeping) {
        this.timeSleeping = timeSleeping;
    }

    @Override
    public boolean isReleasable() {
        return isSleepCompleted;
    }

    @Override
    public boolean block() throws InterruptedException {
        if (!isReleasable()) {
            Thread.sleep(timeSleeping);
            isSleepCompleted = true;
        }
        return true;
    }
}
