package org.trading.orderbook.model.impl.book;


import org.trading.orderbook.model.IOrderBook;
import org.trading.orderbook.model.IOrderConsumerComponent;
import org.trading.orderbook.model.impl.Order;
import org.trading.orderbook.model.impl.Worker;

import java.util.concurrent.CountDownLatch;

/**
 * Encapsulates an order book with its own processing thread.
 */
public class OrderBookWorker implements IOrderConsumerComponent {

    private final IOrderBook orderBook;

    private final Worker worker;

    private final CountDownLatch latch;

    public OrderBookWorker(IOrderBook orderBook) {
        this.orderBook = orderBook;
        latch = new CountDownLatch(1);
        worker = new Worker(latch);
    }

    @Override
    public void start() {
        worker.start();
    }

    @Override
    public void stop() {
        worker.stop();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(final Order order) {
        worker.execute(() -> {
                    orderBook.add(order);
                }
        );
    }

    @Override
    public void delete(final Order order) {
        worker.execute(() -> {
                    orderBook.delete(order);
                }
        );
    }

    @Override
    public void cancel(Order order) {
        worker.execute(() -> {
                    orderBook.cancel(order);
                }
        );
    }

    @Override
    public String toString() {
        return String.valueOf(orderBook);
    }
}
