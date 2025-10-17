package edu.polina.docscountingwords;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class ForkJoinDocTask extends RecursiveTask<Map<String, Long>> {
    DocLoader loader;
    public List<Path> docs;
    int splitThreshold;
    boolean blocker;

    ForkJoinDocTask(List<Path> docs, int splitThreshold, boolean blocker, DocLoader loader) {
        this.docs = docs;
        this.splitThreshold = splitThreshold;
        this.blocker = blocker;
        this.loader = loader;
    }

    @Override
    public Map<String, Long> compute() {
        if (docs.size() < splitThreshold) {
            List<String> generalText = new ArrayList<>();
            if (blocker) {
                for (Path p : docs) {
                    ManagedFileBlocker fileBlocker = new ManagedFileBlocker(p);
                    try {
                        ForkJoinPool.managedBlock(fileBlocker);
                    } catch (InterruptedException _) {
                        Thread.currentThread().interrupt();
                    }
                    generalText.addAll(fileBlocker.getResult());
                }
                try {
                    ForkJoinPool.managedBlock(new ManagedSleepBlocker(3));
                } catch (InterruptedException _) {
                    Thread.currentThread().interrupt();
                }
                List<String> docWords = TextParser.toWords(generalText);
                return docWords.stream().collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            }
            else {
                for (Path p : docs) {
                    generalText.addAll(loader.load(p));
                }
                    List<String> docWords = TextParser.toWords(generalText);
                    return docWords.stream().collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            }
        }

        int halfDocSum = docs.size()/2;
        List<Path> docshalf1 = docs.subList(0,halfDocSum);
        List<Path> docshalf2 = docs.subList(halfDocSum, docs.size());
        ForkJoinDocTask first = new ForkJoinDocTask(docshalf1, splitThreshold, blocker, loader);
        ForkJoinDocTask second = new ForkJoinDocTask(docshalf2, splitThreshold, blocker, loader);
        first.fork();
        Map<String, Long> result = second.compute();
        Map<String, Long> mapHalf = first.join();
        for (Map.Entry<String, Long> pair : mapHalf.entrySet()) {
            result.merge(pair.getKey(), pair.getValue(), Long::sum);
        }
        return result;

    }

}
