package org.trading.orderbook.connectors.upstream.commands.incomming;

import org.trading.orderbook.connectors.commands.IOrderCommand;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.model.Side;
import org.trading.orderbook.dispatch.DispatcherManager;

public class NewOrderCommand implements IOrderCommand {

    private final String orderBook;

    private final Side side;

    private final int size;

    private final double price;

    private final DispatcherManager dispatcherManager;

    public NewOrderCommand(
            DispatcherManager dispatcherManager,
            String orderBook,
            Side side,
            int size,
            double price) {
        this.dispatcherManager = dispatcherManager;
        this.orderBook = orderBook;
        this.side = side;
        this.size = size;
        this.price = price;
    }

    public String getOrderBook() {
        return orderBook;
    }

    public Side getSide() {
        return side;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public void process() {
        Order order = new Order();
        order.setOrderBook(getOrderBook());
        order.setSide(getSide());
        order.setSize(getSize());
        order.setPrice(getPrice());
        dispatcherManager.addOrder(order);
    }

}
