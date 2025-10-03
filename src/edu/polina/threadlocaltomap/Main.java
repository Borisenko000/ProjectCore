package edu.polina.threadlocaltomap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Path path = Paths.get("C:\\Users\\fact.txt");
        List<String> textlines = new ArrayList<>();
        try {
            textlines = Files.readAllLines(path);
            if (textlines.isEmpty()) {
                System.out.println("Файл пустой");
                throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
        }
        int threads = Math.min(Runtime.getRuntime().availableProcessors(), textlines.size());
        int chunkSize = (textlines.size() + threads - 1) / threads;
        List<String> chunkingText = new ArrayList<>(threads);
        for (int from = 0; from < textlines.size(); from += chunkSize) {
            StringBuilder strb = new StringBuilder();
            for (int to = 0; to < chunkSize; to++) {
                if ((from + to) >= textlines.size()) {
                    break;
                }
                strb.append(textlines.get(from + to)).append("\n");
            }
            chunkingText.add(strb.toString());
        }
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        ThreadLocal<Map<String, Long>> threadlocal = ThreadLocal.withInitial(HashMap<String, Long>::new);
        Map<String, Long> global = new ConcurrentHashMap<>(threads);
        List<Future<?>> futures = new ArrayList<>(threads);
        try {
            for (String s : chunkingText) {
                Future<?> future = pool.submit(() -> {
                    Map<String, Long> local = Arrays.stream(s.split("\\s+"))
                            .map(k -> k.toLowerCase().replaceAll("\\p{Punct}", ""))
                            .filter(k -> !k.isEmpty())
                            .collect(Collectors.groupingBy(k -> k, Collectors.counting()));
                    try {
                    threadlocal.set(local);
                        for (Map.Entry<String, Long> e : threadlocal.get().entrySet()) {
                            global.merge(e.getKey(), e.getValue(), Long::sum);
                        }
                    } finally {
                        threadlocal.remove();
                    }
                });
                futures.add(future);
            }
        } finally {
            pool.shutdown();
        }
        try {
            for (Future f : futures) {
                f.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println("Ошибка выполнения задачи" + e.getMessage());
        }
        global.forEach((k, v) -> System.out.println(k + ":" + v));
    }
}
