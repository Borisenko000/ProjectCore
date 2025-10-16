package edu.polina.docscountingwords;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class ForkJoinDocTask extends RecursiveTask<Map<String, Long>> {
    public static List<Path> docs;
    int splitThreshold = 1000;

    ForkJoinDocTask(List<Path> docs, int splitThreshold) {
        this.docs = docs;
        this.splitThreshold = splitThreshold;
    }

    @Override
    public Map<String, Long> compute() {
        ManagedFileBlocker fileBlocker = new ManagedFileBlocker(docs);
        if (docs.size() < splitThreshold) {
            List<String> generalText = new ArrayList<>();
            try {
                ForkJoinPool.managedBlock(fileBlocker);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            generalText = fileBlocker.getResult();
            /* for (Path p : docs) {
                generalText.addAll(DocLoader.load(p));
            }
             */
            try {
                ForkJoinPool.managedBlock(new ManagedSleepBlocker(generalText));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            List<String> docWords = TextParser.toWords(generalText);
            return docWords.stream().collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        }
        List<String> generalText = new ArrayList<>();
        int halfDocSum = DocLoader.docsSum / 2;
        List<Path> docshalf1 = docs.subList(0,halfDocSum);
        List<Path> docshalf2 = docs.subList(halfDocSum, docs.size());
        ForkJoinDocTask first = new ForkJoinDocTask(docshalf1, splitThreshold);
        ForkJoinDocTask second = new ForkJoinDocTask(docshalf2, splitThreshold);
        first.fork();
        Map<String, Long> result = second.compute();
        Map<String, Long> mapHalf = first.join();
        for (Map.Entry<String, Long> pair : result.entrySet()) {
            result.merge(pair.getKey(), pair.getValue(), Long::sum);
        }
        DocLoader.docsSum = 0;
        return result;

    }

}
