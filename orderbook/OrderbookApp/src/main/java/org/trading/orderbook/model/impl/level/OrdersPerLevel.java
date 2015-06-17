package org.trading.orderbook.model.impl.level;

import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.impl.Order;

import java.util.*;

/**
 * Stores all orders from a book belonging to a particular side and price pair.
 * A queue is used to store all these orders so that the oldest order will always
 * be the first one to be processed (filled).
 * Not thread safe !
 */
public class OrdersPerLevel {

    private Queue<Order> orders;

    private int accumulatedVolume;

    private final IModelObserver observer;

    public OrdersPerLevel(IModelObserver observer) {
        this.observer = observer;
        accumulatedVolume = 0;
        orders = new LinkedList<Order>();
    }

    /**
     * Add a new order at tail, being the youngest one.
     *
     * @param order
     */
    public void add(Order order) {
        orders.add(order);
        accumulatedVolume += order.getVolume();
    }

    /**
     * Removes the order if existent
     *
     * @param order the order to be removed
     * @return true if the order existed
     */
    public boolean delete(Order order) {
        if (orders.remove(order)) {
            accumulatedVolume -= order.getVolume();
            observer.onOrderDeleted(order.getOrderId());
            return true;
        }
        return false;
    }

    public int getAccumulatedVolume() {
        return accumulatedVolume;
    }

    public int getOrdersCount() {
        return orders.size();
    }

    /**
     * Given a volume, iterate through orders, from oldest to youngest, and remove them while still volume to be filled
     *
     * @param volumeToFill the volume to be filled
     * @return how much it got actually filled
     */
    public int fill(int volumeToFill) {
        if (volumeToFill <= 0) {
            return 0;
        }
        int volume = volumeToFill;
        Order order;
        while (volumeToFill > 0) {
            order = orders.peek();
            if (order != null) {
                int cOrdVol = order.getVolume();
                if (cOrdVol > volumeToFill) {
                    int volRemaining = cOrdVol - volumeToFill;
                    order.setVolume(volRemaining);
                    observer.onOrderPartiallyFilled(order.getOrderId(), order.getSize(), volumeToFill, volRemaining);
                } else {
                    orders.poll();
                    observer.onOrderFilled(order.getOrderId(), cOrdVol);
                    observer.onOrderDeleted(order.getOrderId());
                }
                volumeToFill -= Math.min(volumeToFill, cOrdVol);
            } else {
                break;
            }
        }
        int volumeFilled = volume - volumeToFill;
        accumulatedVolume -= volumeFilled;
        return volumeFilled;
    }

    /**
     * To be used when the volume to be filled is at least as the accumulated volume of all orders from the queue
     */
    public void fillAll() {
        Order order;
        while ((order = orders.poll()) != null) {
            observer.onOrderFilled(order.getOrderId(), order.getVolume());
            observer.onOrderDeleted(order.getOrderId());
        }
    }
}
