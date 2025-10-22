package edu.polina.tiktak;


public class TikTak {
    public static void main(String[] args) {
        ThreadRunnable trTik = new ThreadRunnable("tik");
        ThreadRunnable trTak = new ThreadRunnable("tak");
        Thread thread1 = new Thread(trTik);
        Thread thread2 = new Thread(trTak);
        thread1.start();
        thread2.start();
    }

}

class ThreadRunnable implements Runnable {
    static Object lock = new Object();
    String word;
    static int i = 1;

    public ThreadRunnable(String word) {
        this.word = word;
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (true) {
                if (i % 2 == 1 && word.equals("tik")) {
                    System.out.println(word);
                    i++;
                    lock.notify();
                    while (i % 2 != 1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                if (i % 2 == 0 && word.equals("tak")) {
                    System.out.println(word);
                    i++;
                    lock.notify();
                    while (i % 2 != 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        }
    }
}
