package com.code.enhancement.apis;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class LogProcessor {
    private final PriorityBlockingQueue<LogTask> logQueue = new PriorityBlockingQueue<>();

    public void produceLog(LogTask task) {
        logQueue.add(task);
    }

    public LogTask consumeLog() throws InterruptedException {
        return logQueue.take();
    }
}

class Producer extends Thread {
    private final LogProcessor processor;

    public Producer(LogProcessor logProcessor) {
        this.processor = logProcessor;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int priority = random.nextInt(5);
            LogTask task = new LogTask("LogProcessor-" + i, priority);
            processor.produceLog(task);
            System.out.println("Producer " + task.getPriority() + " " + task.getPriority());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Consumer implements Runnable {
    private final LogProcessor processor;
    private final int id;

    public Consumer(LogProcessor logProcessor, int id) {
        this.processor = logProcessor;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                LogTask task = processor.consumeLog();
                System.out.println(task.getPriority() + " " + task.getPriority());
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


class LogTask implements Comparable<LogTask> {
    private final String message;
    private final int priority; // Lower value = higher priority

    public LogTask(String message, int priority) {
        this.message = message;
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(LogTask other) {
        return Integer.compare(this.priority, other.priority);
    }
}


class LogProcessingApp {
    public static void main(String[] args) {
        LogProcessor processor = new LogProcessor();

        Producer producer = new Producer(processor);
        producer.start();

        ExecutorService consumerPool = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 3; i++) {
            consumerPool.submit(new Consumer(processor, i));
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumerPool.shutdownNow();
            System.out.println("LogProcessingApp shutting down...");
        }));
    }
}


