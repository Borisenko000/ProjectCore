package edu.polina.orderservice;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Order implements Delayed {
    final String id;
    long expireMillis;

    public Order(String id, long delayMillis) {
        this.id = id;
        expireMillis = System.currentTimeMillis() + delayMillis;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(expireMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Math.toIntExact(this.expireMillis - ((Order) o).expireMillis);
    }
}
