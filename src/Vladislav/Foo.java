package Vladislav;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class Foo {
    Semaphore semaphore;
    Semaphore semaphore1;

    Foo(Semaphore semaphore, Semaphore semaphore1) {
        this.semaphore = semaphore;
        this.semaphore1 = semaphore1;

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

class Check{
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        Semaphore semaphore0 = new Semaphore(1);

        Foo foo = new Foo(semaphore, semaphore0);

//        foo.second(new Th("second"));
//        foo.first(new Th("first"));
//        foo.third(new Th("third"));

        CompletableFuture.runAsync(() -> {
            foo.second(new Thread(() -> System.out.println("second")));
        });

        CompletableFuture.runAsync(() -> {
            foo.first(new Thread(() -> System.out.println("first")));
        });

        CompletableFuture.runAsync(() -> {
            foo.third(new Thread(() -> System.out.println("third")));
        });

    }
}

//class Th implements Runnable {
//    Thread thread;
//    String message;
//
//    Th (String message) {
//        new Thread(() -> System.out.print(message)).start();
//    }
//
//    @Override
//    public void run() {
//    }
//}

