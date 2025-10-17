package edu.polina.docscountingwords;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Path> docs = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Path path = Paths.get("C:\\Test\\Текстовый документ " + i + ".txt");
            docs.add(path);
        }
        DocLoader loader = new DocLoader();
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory(), null, true);
        try {
            ForkJoinDocTask task = new ForkJoinDocTask(docs, 1000, false, loader);
            long startTime = System.nanoTime();
            Map<String, Long> map1 = pool.invoke(task);
            long endTime = System.nanoTime();
            map1.forEach((k, v) -> System.out.println(k + ":" + v));
            System.out.println("Время выполнения задачи без FJP: " + (endTime - startTime));

            ForkJoinDocTask task2 = new ForkJoinDocTask(docs, 1000, true, loader);
            startTime = System.nanoTime();
            Map<String, Long> map2 = pool.invoke(task2);
            endTime = System.nanoTime();
            map2.forEach((k, v) -> System.out.println(k + ":" + v));
            System.out.println("Время выполнения задачи c FJP: " + (endTime - startTime));

            startTime = System.nanoTime();
            Map<String, Long> map3 = docs.parallelStream()
                    .map(loader::load)
                    .map(TextParser::toWords)
                    .flatMap(List::stream)
                    .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
            endTime = System.nanoTime();
            map3.forEach((k, v) -> System.out.println(k + ":" + v));
            System.out.println("Время выполнения задачи с parallelStream: " + (endTime - startTime));
        } finally {
            pool.shutdown();
            try {
                pool.awaitTermination(5, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
