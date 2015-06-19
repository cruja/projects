package org.trading.orderbook.model.impl.book;

import org.trading.orderbook.connectors.upstream.UpstreamManager;
import org.trading.orderbook.gui.OrderBookGui;
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
            book.start();
        }
    }

    public void finishProcessing() {
        for (IOrderConsumerComponent book : map.values()) {
            book.stop();
        }
    }

    public IOrderConsumerComponent find(String bookName) {
        return map.get(bookName);
    }

    public IOrderConsumerComponent create(
            String bookName) {

        IOrderBook orderBook = new OrderBookImpl(
                bookName);
        orderBook.register(UpstreamManager.getInstance().getOutgoingStream());
        if (true) {
            orderBook.register(
                    new OrderBookGui(bookName));
        }
        IOrderConsumerComponent orderBookWorker = new OrderBookWorker(orderBook);
        orderBookWorker.start();
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
