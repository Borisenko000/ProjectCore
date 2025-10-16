package edu.polina.docscountingwords;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ManagedSleepBlocker implements ForkJoinPool.ManagedBlocker {
    List<String> generalText = new ArrayList<>();

    public ManagedSleepBlocker(List<String> generalText) {
        this.generalText = generalText;
    }

    @Override
    public boolean isReleasable() {
        return generalText.isEmpty();
    }

    @Override
    public boolean block() throws InterruptedException {
        if (!isReleasable()) {
            Thread.sleep(3);
            generalText.clear();
        }
        return true;
    }
}
