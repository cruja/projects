package org.trading.orderbook.model.impl.book;

import org.trading.orderbook.model.IAggregatorListener;
import org.trading.orderbook.model.IOrderBook;
import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.impl.Order;
import org.trading.orderbook.model.impl.OrderIdContainer;
import org.trading.orderbook.model.impl.level.PriceLevelAggregator;

import java.util.*;

/**
 * The order book main component.
 * This class is not thread safe.
 */
public class OrderBookImpl implements IOrderBook {

    private final String name;

    private final OrderBookDataModel dataModel;

    // aggregator is an observer for the order book data model
    // will be used for the application output
    // if enabled, will also be used as a data source for the GUI component
    private final PriceLevelAggregator aggregator;

    // an order with same ID as one existent can not be added
    // an order with same ID as one existent will have no effect when deleted
    private final OrderIdContainer idContainer;

    /**
     * @param name         the name of the order book
     */
    public OrderBookImpl(String name) {
        this.name = name;
        aggregator = new PriceLevelAggregator(name);
        idContainer = new OrderIdContainer();
        dataModel = new OrderBookDataModel(idContainer);
        dataModel.register(aggregator);
    }

    @Override
    public void register(IModelObserver modelObserver) {
        dataModel.register(modelObserver);
    }

    @Override
    public void register(IAggregatorListener aggregatorObserver) {
        aggregator.register(aggregatorObserver);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    /**
     * Add a new order to the current book.
     *
     * @param order the order to be added
     */
    public void add(Order order) {
        dataModel.add(order);
    }

    /**
     * Deletes the order from the current book
     *
     * @param order the order to be deleted
     */
    public void delete(Order order) {
        dataModel.delete(order);
    }

    @Override
    public void cancel(Order order) {
        dataModel.cancel(order);
    }


    // -------------------------------------------------------------------------
    // Methods to be used only for test purposes after the feed has completed !!!
    // -------------------------------------------------------------------------

    /**
     * The lowest price on the specified price at which there is an order in the current book
     *
     * @param isBuySide the side
     * @return the lowest price
     */
    public double getLowestPrice(boolean isBuySide) {
        return dataModel.getLowestPrice(isBuySide);
    }

    /**
     * The highest price on the specified price at which there is an order in the current book
     *
     * @param isBuySide the side
     * @return the highest price
     */
    public double getHighestPrice(boolean isBuySide) {
        return dataModel.getHighestPrice(isBuySide);
    }

    /**
     * The lowest price on sell side that has an order or the highest price on buy side that has an order
     * An order at best price is going to be the first one filled when needed
     *
     * @param isBuySide whether buy or sell side
     * @return the best price
     */
    public double getBestPrice(boolean isBuySide) {
        return dataModel.getBestPrice(isBuySide);
    }

    /**
     * The sum of all order volumes on the specified side
     *
     * @param isBuySide whether buy or sell side
     * @return the summed up volume
     */
    public int getAccVol(boolean isBuySide) {
        return dataModel.getAccVol(isBuySide);
    }

    /**
     * The number of orders on all prices on the specified side
     *
     * @param isBuySide whether buy or sell side
     * @return the total
     */
    public int getOrdersCount(boolean isBuySide) {
        return dataModel.getOrdersCount(isBuySide);
    }

    /**
     * The number of orders on the specified price on the specified side
     *
     * @param price
     * @param isBuySide
     * @return
     */
    public int getOrdersCount(double price, boolean isBuySide) {
        return dataModel.getOrdersCount(price, isBuySide);
    }

    /**
     * The sum of all order volumes at the specified price on the specified side
     *
     * @param price
     * @param isBuySide
     * @return
     */
    public int getAccVolume(double price, boolean isBuySide) {
        return dataModel.getAccVolume(price, isBuySide);
    }

    /**
     * A list with all order IDs currently active
     *
     * @return
     */
    public List<Long> getOrderIds() {
        return idContainer.getOrderIds();
    }

    @Override
    public String toString() {
        return aggregator.toString();
    }
}
