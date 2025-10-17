package edu.polina.docscountingwords;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

public class ThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {

    @Override
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
        thread.setName("fjp-docs-" + thread.getPool().getPoolSize());
        return thread;
    }
}
