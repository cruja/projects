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
    public void handleEvent(Action action, Order order, IModelObserver modelObserver) throws InvalidEventException {
        if (action == null) {
            throw new InvalidEventException("Action is not allowed to be null");
        }
        if (order == null) {
            throw new InvalidEventException("Order is not allowed to be null");
        }
        switch (action) {
            case ADDORDER:
                add(order, modelObserver);
                break;
            case DELETEORDER:
                delete(order, modelObserver);
                break;
            case CANCELORDER:
                cancel(order, modelObserver);
                break;
        }
    }

    private void add(Order order, IModelObserver modelObserver) throws InvalidEventException {
        final String bookName = order.getOrderBook();
        IOrderConsumerComponent orderBook = getBook(bookName, modelObserver);
        orderBook.add(order);
    }

    private void delete(Order order, IModelObserver modelObserver) {
        final String bookName = order.getOrderBook();
        IOrderConsumerComponent orderBook = getBook(bookName, modelObserver);
        orderBook.delete(order);
    }

    private void cancel(Order order, IModelObserver modelObserver) {
        final String bookName = order.getOrderBook();
        IOrderConsumerComponent orderBook = getBook(bookName, modelObserver);
        orderBook.delete(order);
    }


    private IOrderConsumerComponent getBook(String bookName, IModelObserver modelObserver) {
        IOrderConsumerComponent orderBook = orderBookManager.find(bookName);
        if (orderBook == null) {
            orderBook = orderBookManager.create(
                    bookName,
                    modelObserver);
        }
        return orderBook;
    }
}
