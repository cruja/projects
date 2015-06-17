package org.trading.orderbook.model.impl.level;

public class PriceLevel {

    private final OrdersPerLevel[] ordersPerLevels;

    public PriceLevel() {
        ordersPerLevels = new OrdersPerLevel[2]; // 0 for SELL, 1 for BUY
    }

    public int getAccVol(int side) { // 0 for SELL, 1 for BUY
        return ordersPerLevels[side] == null ? 0 : ordersPerLevels[side].getAccumulatedVolume();
    }

    public int getOrdCnt(int side) { // 0 for SELL, 1 for BUY){
        return ordersPerLevels[side] == null ? 0 : ordersPerLevels[side].getOrdersCount();
    }

    public OrdersPerLevel[] getOrdersPerLevels() {
        return ordersPerLevels;
    }

    /**
     * Updates the references to OrdersPerLevel queues
     *
     * @param side
     * @param ordersPerLevel
     * @return true if this price level still references non-null queues
     */
    public boolean onChange(int side, OrdersPerLevel ordersPerLevel) {
        ordersPerLevels[side] = ordersPerLevel;
        if ((ordersPerLevels[0] == null || ordersPerLevels[0].getOrdersCount() == 0)
                && (ordersPerLevels[1] == null || ordersPerLevels[1].getOrdersCount() == 0)) {
            return false;
        }
        return true;
    }

}
