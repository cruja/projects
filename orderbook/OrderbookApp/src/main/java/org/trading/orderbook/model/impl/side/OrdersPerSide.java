package org.trading.orderbook.model.impl.side;

import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.impl.Order;
import org.trading.orderbook.model.impl.level.OrdersPerLevel;

import java.util.*;

/**
 * Store all orders on one side of an order book.
 * Not thread safe !!!
 */
public abstract class OrdersPerSide {

    private final String sideStr;

    private final int id;

    // orders are grouped in queues per price level
    // for efficiency, these queues are stored in a balanced tree data structure
    private TreeMap<Double, OrdersPerLevel> priceToOrderLevelsMap;

    private double lowestPrice = Double.NaN;

    private double highestPrice = Double.NaN;

    private int accumulatedVolume;

    private final IModelObserver observer;

    /**
     * Whether on order at the provided price on the other side than the current one
     * would fill some orders at best price on the current side
     *
     * @param price
     * @return true for matching, false otherwise
     */
    public abstract boolean isMatch(double price);

    /**
     * The best price on the current side, which is the one from which orders on the current side are used to fill
     * orders at the other side.
     *
     * @return the best price
     */
    public abstract double getBestPrice();

    protected OrdersPerSide(String sideStr, int id, IModelObserver observer) {
        priceToOrderLevelsMap = new TreeMap<Double, OrdersPerLevel>();
        this.sideStr = sideStr;
        this.id = id;
        this.observer = observer;
    }

    public void add(Order order) {
        double price = order.getPrice();
        OrdersPerLevel ordersPerLevel = get(price);
        ordersPerLevel.add(order);
        accumulatedVolume += order.getVolume();
        refresh();
    }

    public void delete(Order order) {
        double price = order.getPrice();
        OrdersPerLevel ordersPerLevel = get(price);
        if (ordersPerLevel.delete(order)) {
            accumulatedVolume -= order.getVolume();
            if (ordersPerLevel.getOrdersCount() == 0) {
                priceToOrderLevelsMap.remove(price);
            }
        }
        refresh();
    }

    public int getId() {
        return id;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public double getHighestPrice() {
        return highestPrice;
    }


    public int getAccumulatedVolume() {
        return accumulatedVolume;
    }

    public int getAccumulatedVolume(double price) {
        OrdersPerLevel level = priceToOrderLevelsMap.get(price);
        return level == null ? 0 : level.getAccumulatedVolume();
    }

    public int getOrdersCount() {
        int total = 0;
        for (OrdersPerLevel level : priceToOrderLevelsMap.values()) {
            total += level.getOrdersCount();
        }
        return total;
    }

    public int getOrdersCount(double price) {
        OrdersPerLevel level = priceToOrderLevelsMap.get(price);
        if (level == null) {
            return 0;
        }
        return level.getOrdersCount();
    }

    /**
     * Iterates over the side's prices from best to worst and fills the orders on those levels from oldest to youngest
     *
     * @param volumeToFill the volume that needs be filled
     * @return how much got actually filled
     */
    public int fill(int volumeToFill, double price) {
        int volume = volumeToFill;
        while (volumeToFill > 0 && isMatch(price)) {
            double bestPrice = getBestPrice();
            if (bestPrice != bestPrice) { // NaN
                return 0;
            }
            OrdersPerLevel ordersOnBestPrice = priceToOrderLevelsMap.get(bestPrice);
            int accVolOnBestPrice = ordersOnBestPrice.getAccumulatedVolume();
            int filled = ordersOnBestPrice.fill(volumeToFill);
            accumulatedVolume -= filled;
            if (accVolOnBestPrice == filled) {
                priceToOrderLevelsMap.remove(bestPrice);

            }
            observer.onUpdate(this, bestPrice);
            refresh();
            volumeToFill -= filled;
        }

        return volume - volumeToFill;
    }

    /**
     *
     */
    public void fillAll() {

        for (Map.Entry<Double, OrdersPerLevel> entry : priceToOrderLevelsMap.entrySet()) {
            entry.getValue().fillAll();
            observer.onUpdate(this, entry.getKey());
        }

        priceToOrderLevelsMap.clear();
        accumulatedVolume = 0;
        refresh();
    }

    public TreeMap<Double, OrdersPerLevel> getPriceToOrderLevelsMap() {
        return priceToOrderLevelsMap;
    }

    private void refresh() {
        try {
            lowestPrice = priceToOrderLevelsMap.firstKey();
            highestPrice = priceToOrderLevelsMap.lastKey();
        } catch (NoSuchElementException e) {
            lowestPrice = Double.NaN;
            highestPrice = Double.NaN;
        }
    }

    private OrdersPerLevel get(double price) {
        OrdersPerLevel ordersPerLevel = priceToOrderLevelsMap.get(price);
        if (ordersPerLevel == null) {
            ordersPerLevel = new OrdersPerLevel(observer);
            priceToOrderLevelsMap.put(price, ordersPerLevel);
            if (lowestPrice != lowestPrice || price < lowestPrice) {
                lowestPrice = price;
            }
            if (highestPrice != highestPrice || price > highestPrice) {
                highestPrice = price;
            }
        }
        return ordersPerLevel;
    }
}
