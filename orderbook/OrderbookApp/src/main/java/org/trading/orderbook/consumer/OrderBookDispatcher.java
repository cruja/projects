package org.trading.orderbook.consumer;

import org.trading.orderbook.consumer.exception.InvalidEventException;
import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.IOrderConsumerComponent;
import org.trading.orderbook.model.IOrderProcessor;
import org.trading.orderbook.model.impl.Action;
import org.trading.orderbook.model.impl.Order;
import org.trading.orderbook.model.impl.book.OrderBookManager;

import java.util.logging.Logger;

/**
 * The main consumer of actions on orders.
 */
public class OrderBookDispatcher implements IOrderProcessor {

    private static Logger LOGGER = Logger.getLogger(OrderBookDispatcher.class.getCanonicalName());

    private OrderBookManager orderBookManager;

    public OrderBookDispatcher() {
        orderBookManager = new OrderBookManager();
    }

    @Override
    public void startProcessing() {
        orderBookManager.startProcessing();
    }

    @Override
    public void finishProcessing() {
        orderBookManager.finishProcessing();
    }

    @Override
    public void handleEvent(Action action, Order order) throws InvalidEventException {
        if (action == null) {
            throw new InvalidEventException("Action is not allowed to be null");
        }
        if (order == null) {
            throw new InvalidEventException("Order is not allowed to be null");
        }
        switch (action) {
            case ADDORDER:
                add(order);
                break;
            case DELETEORDER:
                delete(order);
                break;
            case CANCELORDER:
                cancel(order);
                break;
        }
    }

    private void add(Order order) throws InvalidEventException {
        final String bookName = order.getOrderBook();
        IOrderConsumerComponent orderBook = getBook(bookName);
        orderBook.add(order);
    }

    private void delete(Order order) {
        final String bookName = order.getOrderBook();
        IOrderConsumerComponent orderBook = getBook(bookName);
        orderBook.delete(order);
    }

    private void cancel(Order order) {
        final String bookName = order.getOrderBook();
        IOrderConsumerComponent orderBook = getBook(bookName);
        orderBook.delete(order);
    }


    private IOrderConsumerComponent getBook(String bookName) {
        IOrderConsumerComponent orderBook = orderBookManager.find(bookName);
        if (orderBook == null) {
            orderBook = orderBookManager.create(
                    bookName
            );
        }
        return orderBook;
    }
}
