package Vladislav;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class Foo {
    Semaphore semaphore = new Semaphore(0);
    Semaphore semaphore1 = new Semaphore(0);

    public void first(Runnable r) {
        System.out.print("first");
        semaphore.release();
    }

    public void second(Runnable r) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            System.out.println("Blocking: " + e);
        }
        System.out.print("second");
        semaphore1.release();
    }

    public void third(Runnable r) {
        try {
            semaphore1.acquire();
        } catch (InterruptedException e) {
            System.out.println("Blocking: " + e);
        }
        System.out.print("third");
    }
}

class Check {
    public static void main(String[] args) {
        Foo foo = new Foo();

        CompletableFuture.runAsync(() -> {
            foo.second(new Thread());
        });

        CompletableFuture.runAsync(() -> {
            foo.first(new Thread());
        });

        CompletableFuture.runAsync(() -> {
            foo.third(new Thread());
        });

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


