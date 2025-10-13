package edu.polina.orderservice;

import java.util.Random;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderSystem {
    private static final Logger LOGGER = Logger.getLogger(OrderSystem.class.getName());

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            4, 8, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    public static CompletableFuture<Void> process(Order order) {
        return CompletableFuture
                .supplyAsync(() -> pay(order), executor)
                .orTimeout(10, TimeUnit.SECONDS)
                .exceptionally(ex -> {LOGGER.log(Level.SEVERE, "Timeout for " + order.id, ex);
                    return null;})
                .thenCompose(OrderSystem::ship)
                .thenAccept(OrderSystem::notifyUser)
                .exceptionally(ex -> { LOGGER.log(Level.SEVERE, "Ошибка обработки заказа", ex); return null;});

    }

    private static int randomDelay(long timeFrom, long timeTo) {
        Random rn = new Random();
        return rn.nextInt((int) (Math.toIntExact(timeTo - timeFrom + 1) + timeFrom));
    }

    private static String pay(Order o) {
        o.expireMillis += randomDelay(1000, 3000) ;
        return new String(o.id);
    }

    private static CompletableFuture<String> ship(Object payment) {
        System.out.println("Идет обработка заказа " + (String) payment);
        return CompletableFuture.supplyAsync(() -> ((String) payment));
    }

    private static void notifyUser(String shipment) {
        System.out.println("Обработка заказа  " + shipment + " завершена");
    }

    static void checkStatus() {
        System.out.printf("active=%d queued=%d completed=%d%n",
                executor.getActiveCount(),
                executor.getQueue().size(),
                executor.getCompletedTaskCount());
    }
}
