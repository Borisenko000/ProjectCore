package edu.polina.timerthreads;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        int threads = Runtime.getRuntime().availableProcessors();
        LinkedBlockingDeque<Map<String, Long>> queue = new LinkedBlockingDeque<>(threads);
        Map<String, Long> global = new ConcurrentHashMap<>(threads);
        for (int i = 0; i < threads; i++) {
            Thread thread = new Thread(() -> {
                Map<String, Long> local = Arrays.stream("C:\\Users\\Professional\\java-projects\\github\\ProjectCore>".split("\\\\"))
                        .map(k -> k.toLowerCase().replaceAll("\\p{Punct}", ""))
                        .filter(k -> !k.isEmpty())
                        .collect(Collectors.groupingBy(k -> k, Collectors.counting()));
                queue.add(local);
                for (Map<String, Long> m : queue) {
                    for(Map.Entry<String, Long> p: m.entrySet()) {
                        global.merge(p.getKey(), p.getValue(), Long::sum);
                    }
                }
            });
            try {
                thread.start();
                thread.join(500);
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
            } finally {
                global.forEach(((k, v) -> System.out.println(k + ":" + v)));
            }
        }

    }
}
