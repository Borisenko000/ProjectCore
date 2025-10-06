package edu.polina.threads5steps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class StepOne {
    public static void main(String[] args) {
        int n = 100;
        List<Integer> range = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            range.add(i);
        }
        int threads = Math.min(Runtime.getRuntime().availableProcessors(), n);
        int chunk = (n + threads - 1) / threads;
        List<List<Integer>> chunks = new ArrayList<>(threads);
        for (int from = 0; from < range.size(); from += chunk) {
            List<Integer> chunkList = new ArrayList<>();
            for (int to = 0; to < chunk && from + to < range.size(); to++) {
                chunkList.add(range.get(from + to));
            }
            chunks.add(chunkList);
        }
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch count = new CountDownLatch(chunks.size());
        List<Future<Integer>> futures = new ArrayList<>(threads);
        for (List<Integer> c : chunks) {
            Future<Integer> f = pool.submit(() -> {
                try {
                    return c.stream().mapToInt(Integer::intValue).sum();
                } finally {
                    count.countDown();
                }
            });
            futures.add(f);
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int total = 0;
        try {
            for (Future<Integer> f : futures) {
                total += f.get();
            }
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
            return;
        } catch (ExecutionException e) {
            System.out.println("Ошибка выполнения задачи" + e.getCause());
        } finally {
            pool.shutdown();
        }
        System.out.println(total);
    }
}
