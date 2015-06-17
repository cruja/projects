package org.trading.orderbook.connectors.upstream.commands.incomming;

import org.trading.orderbook.connectors.commands.IOrderCommand;
import org.trading.orderbook.model.Order;
import org.trading.orderbook.model.Side;
import org.trading.orderbook.dispatch.DispatcherManager;

public class NewOrderRangeCommand implements IOrderCommand {

    private final String orderBook;
    private final Side side;
    private final int size;
    private final int levels;
    private final double step;
    private final double price;
    private final int direction;
    private final DispatcherManager dispatcherManager;

    public NewOrderRangeCommand(
            DispatcherManager dispatcherManager,
            String orderBook, Side side, int size, int levels,
            double step, double price, int direction) {
        this.dispatcherManager = dispatcherManager;
        this.orderBook = orderBook;
        this.side = side;
        this.size = size;
        this.levels = levels;
        this.step = step;
        this.price = price;
        this.direction = direction;
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

    public int getLevels() {
        return levels;
    }

    public double getStep() {
        return step;
    }

    public double getPrice() {
        return price;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public void process() {
        double price = getPrice();
        for (int i = 0; i < getLevels(); i++) {
            Order order = new Order();
            order.setOrderBook(getOrderBook());
            order.setSide(getSide());
            order.setSize(getSize());
            order.setPrice(price);
            dispatcherManager.addAndPlaceOrder(order);
            price += getDirection() * getStep();
        }
    }

}
