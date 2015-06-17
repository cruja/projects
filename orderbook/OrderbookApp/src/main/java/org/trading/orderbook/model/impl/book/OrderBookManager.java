package org.trading.orderbook.model.impl.book;

import org.trading.orderbook.gui.OrderBookGui;
import org.trading.orderbook.model.IContext;
import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.IOrderBook;
import org.trading.orderbook.model.IOrderConsumerComponent;

import java.util.TreeMap;

/**
 * Container for order books.
 * This implementation is not thread-safe.
 */
public class OrderBookManager {

    // guaranteed log(n) time cost for the containsKey, get, put and remove operations
    private final TreeMap<String, IOrderConsumerComponent> map;

    public OrderBookManager() {
        this.map = new TreeMap<>();
    }

    public void startProcessing() {
        for (IOrderConsumerComponent book : map.values()) {
            book.onStart();
        }
    }

    public void finishProcessing() {
        for (IOrderConsumerComponent book : map.values()) {
            book.onStop();
        }
    }

    public IOrderConsumerComponent find(String bookName) {
        return map.get(bookName);
    }

    public IOrderConsumerComponent create(
            String bookName,
            IContext context,
            IModelObserver modelObserver) {

        IOrderBook orderBook = new OrderBookImpl(
                bookName,
                context.isLogging());
        orderBook.register(modelObserver);
        if (context.isClientMode()) {
            orderBook.registerForAggregator(
                    new OrderBookGui(bookName, context));
        }
        IOrderConsumerComponent orderBookWorker = new OrderBookWorker(orderBook);
        orderBookWorker.onStart();
        map.put(bookName, orderBookWorker);
        return orderBookWorker;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (IOrderConsumerComponent book : map.values()) {
            String bookStr = book.toString();
            buffer.append(bookStr).append("\n");
        }
        return buffer.toString();
    }
}
