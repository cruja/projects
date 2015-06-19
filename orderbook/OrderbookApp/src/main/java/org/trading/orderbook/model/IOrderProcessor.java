package org.trading.orderbook.model;

import org.trading.orderbook.model.impl.Order;
import org.trading.orderbook.consumer.exception.InvalidEventException;
import org.trading.orderbook.model.impl.Action;

public interface IOrderProcessor {

    /**
     * Called by environment before any events are processed.
     */
    void startProcessing();

    /**
     * Handles specific event with order data. The meaningful properties of the order depends on the
     * action. Meaningful props are:
     * <ul>
     * <li>ADD: orderId, book name, isBuy, volume, and price</li>
     * <li>DELETE: orderId</li>
     *
     * @param action The action.
     * @param order  The order.
     * @throws InvalidEventException Whenever the above properties are not meaningful for the specific action
     */

    void handleEvent(Action action, Order order) throws InvalidEventException;

    /**
     * Called by the environment when no more events will be handled.
     */
    void finishProcessing();
}
