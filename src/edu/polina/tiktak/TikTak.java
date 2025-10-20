package edu.polina.tiktak;


public class TikTak {
    public static void main(String[] args) {
        ThreadRunnable tr = new ThreadRunnable();
        Thread thread1 = new Thread(tr);
        Thread thread2 = new Thread(tr);
        thread1.start();
        thread2.start();
    }

}

class ThreadRunnable implements Runnable {
    Object lock = new Object();
    int i = 0;
    @Override
    public void run() {
        synchronized (lock) {
            while (true) {
                if (i % 2 == 0) {
                    System.out.println("тик " + Thread.currentThread().getName());
                    i++;
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (i % 2 == 1) {
                    System.out.println("так " + Thread.currentThread().getName());
                    i++;
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException _) {
                        Thread.currentThread().interrupt();
                    }

                }
            }
        }
    }
}
