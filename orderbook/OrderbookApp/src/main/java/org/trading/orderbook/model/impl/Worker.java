package org.trading.orderbook.model.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The book's processing thread that acts as a consumer of Runnable tasks.
 * It keeps taking tasks from its queue and processing them.
 * The thread will wait while there are no tasks into the queue.
 * This worker gets interrupted in 2 ways: by a Throwable thrown during the processing of a task or by interrupting it.
 * The usual way to interact with this worker is to start it, then keep putting tasks into its queue for processing and
 * when you decide it's over, just call its stop method which will make the thread interrupt itself once all existing
 * tasks are processed. New tasks will not be processed anymore.
 */
public class Worker {

    private static final int CAPACITY = 10000;

    private static Logger LOGGER = Logger.getLogger(Worker.class.getCanonicalName());

    private final BlockingQueue<Runnable> jobs;

    private final Thread thread;

    private final CountDownLatch latch;

    private final AtomicBoolean stopGuard;

    private final AtomicBoolean startGuard;

    private final Runnable runnable;

    private final Runnable stop;

    public Worker(CountDownLatch latch) {
        this.latch = latch;
        this.stopGuard = new AtomicBoolean();
        this.startGuard = new AtomicBoolean();
        this.jobs = new ArrayBlockingQueue<>(CAPACITY);
        this.stop =
                () -> {
                    Thread.currentThread().interrupt();
                };
        this.runnable =
                () -> {
                    try {
                        while (true) {
                            Runnable job = this.jobs.take();
                            job.run();
                        }
                    } catch (Throwable t) {
                        LOGGER.log(Level.SEVERE, "Worker stopped executing tasks", t);
                    } finally {
                        latch.countDown();
                    }
                };
        this.thread = new Thread(this.runnable);
    }

    public void start() {
        if (this.startGuard.compareAndSet(false, true)) {
            this.thread.setDaemon(true);
            this.thread.start();
        } else {
            LOGGER.log(Level.WARNING, "Worker already started.");
        }
    }

    public void stop() {
        if (this.stopGuard.compareAndSet(false, true)) {
            try {
                this.jobs.put(stop);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        } else {
            LOGGER.log(Level.WARNING, "Worker already stopped.");
        }
    }

    public void execute(Runnable task) {
        try {
            this.jobs.put(task);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
