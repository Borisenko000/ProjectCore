package edu.polina.orderservice;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.DelayQueue;
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
            int expireMillis = rn.nextInt((int) Math.toIntExact((13000 - 100 + 1) + 100));
            queue.put(new Order("ORD-" + i, expireMillis));
        }
        try {
            while (!queue.isEmpty()) {
                process(queue.take()).join();
                if (executor.getCompletedTaskCount() % 100 == 0) {
                    OrderSystem.checkStatus();
                }
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
