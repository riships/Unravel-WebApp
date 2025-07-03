package com.code.enhancement.apis;

public class DeadlockSimulator {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    // Helper to acquire locks in consistent order
    private void acquireLocks(Object firstLock, Object secondLock) {
        synchronized (firstLock) {
            synchronized (secondLock) {
                System.out.println(Thread.currentThread().getName() + ": Acquired both locks");
            }
        }
    }

    public void method1() {
        acquireLocks(lock1, lock2); // Always lock lock1 first
    }

    public void method2() {
        acquireLocks(lock1, lock2); // Also lock lock1 first (was lock2)
    }

    public static void main(String[] args) {
        DeadlockSimulator simulator = new DeadlockSimulator();
        Thread thread1 = new Thread(simulator::method1, "Thread-1");
        Thread thread2 = new Thread(simulator::method2, "Thread-2");
        thread1.start();
        thread2.start();
    }
}



