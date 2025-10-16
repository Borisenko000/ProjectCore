package edu.polina.orderservice;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static edu.polina.orderservice.OrderSystem.executor;
import static edu.polina.orderservice.OrderSystem.process;

public class Main {
    public static void main(String[] args) {
        DelayQueue<Order> queue = new DelayQueue<>();
        queue.put(new Order("ORD-Test1", 5000));
        queue.put(new Order("ORD-Test2", 10000));
        for (int i = 1; i <= 300; i++) {
            Random rn = new Random();
            int expireMillis = ThreadLocalRandom.current().nextInt(100, 1300);
            queue.put(new Order("ORD-" + i, expireMillis));
        }
        try {
            List<CompletableFuture> futures = new ArrayList<>();
            while (!queue.isEmpty()) {
                futures.add(process(queue.take()));
                if (executor.getCompletedTaskCount() % 100 == 0) {
                    OrderSystem.checkStatus();
                }
            }
            for (CompletableFuture<Void> cf : futures) {
                cf.join();
            }
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        } finally {
            OrderSystem.checkStatus();
            executor.shutdown();
            try {
                executor.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
