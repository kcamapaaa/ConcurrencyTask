package Vladislav;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class Foo {
    Semaphore semaphore = new Semaphore(1);
    Semaphore semaphore1 = new Semaphore(1);

    Foo() {
        try {
            semaphore.acquire();
            semaphore1.acquire();
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e);
        }
    }

    public void first(Runnable r) {
        r.run();
        semaphore.release();
    }

    public void second(Runnable r) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e);
        }
        r.run();
        semaphore1.release();
    }

    public void third(Runnable r) {
        try {
            semaphore1.acquire();
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e);
        }
        r.run();
    }
}

class Check {
    public static void main(String[] args) {
        Foo foo = new Foo();

        CompletableFuture.runAsync(() -> {
            foo.second(new Thread(() -> System.out.print("second")));
        });

        CompletableFuture.runAsync(() -> {
            foo.first(new Thread(() -> System.out.print("first")));
        });

        CompletableFuture.runAsync(() -> {
            foo.third(new Thread(() -> System.out.print("third")));
        });

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


