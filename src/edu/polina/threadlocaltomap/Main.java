package edu.polina.threadlocaltomap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Path path = Paths.get("C:\\Users\\fact.txt");
        List<String> text = new ArrayList<>();
        try {
            text = Files.readAllLines(path);
            if (text.isEmpty()) {
                System.out.println("Файл пустой");
                throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
        }
        int threads = Math.min(Runtime.getRuntime().availableProcessors(), text.size());
        int chunkSize = (text.size() + threads - 1) / threads;
        List<String> finalText = new ArrayList<>(threads);
        for (int from = 0; from < text.size(); from += chunkSize) {
            StringBuilder strb = new StringBuilder();
            for (int to = 0; to < chunkSize; to++) {
                if ((from + to) >= text.size()) {
                    break;
                }
                strb.append(text.get(from + to)).append("\n");
            }
            finalText.add(strb.toString());
        }
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        ThreadLocal<Map<String, Long>> threadlocal = new ThreadLocal<>();
        Map<String, Long> global = new HashMap<>();
        try {
            for (String s : finalText) {
                pool.submit(() -> {
                    Map<String, Long> local = Arrays.stream(s.split("\\s+"))
                            .map(k -> k.toLowerCase().replaceAll("\\p{Punct}", ""))
                            .filter(k -> !k.isEmpty())
                            .collect(Collectors.groupingBy(k -> k, Collectors.counting()));
                    threadlocal.set(local);
                    for (Map.Entry<String, Long> e : threadlocal.get().entrySet()) {
                        global.merge(e.getKey(), e.getValue(), Long::sum);
                    }
                    threadlocal.remove();
                });
            }
        } finally {
            pool.shutdown();
        }
        global.forEach((k, v) -> System.out.println(k + ":" + v));

    }
}
