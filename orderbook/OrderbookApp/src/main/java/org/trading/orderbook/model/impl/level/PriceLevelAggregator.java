package org.trading.orderbook.model.impl.level;


import org.trading.orderbook.model.IAggregatorListener;
import org.trading.orderbook.model.impl.DefaultModelObserver;
import org.trading.orderbook.model.impl.side.OrdersPerSide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PriceLevelAggregator extends DefaultModelObserver {

    private final String name;

    private final TreeMap<Double, PriceLevel> priceLevels;

    private final List<IAggregatorListener> listeners;

    public PriceLevelAggregator(String name) {
        this.name = name;
        priceLevels = new TreeMap<>();
        listeners = new ArrayList<>();
    }

    public void register(IAggregatorListener aggregatorObserver) {
        listeners.add(aggregatorObserver);
    }

    @Override
    public void onAdded(int sellOrdCnt, int sellAccVol, double price, int buyAccVol, int buyOrdCnt) {
        priceLevels.put(price, new PriceLevel());
    }

    @Override
    public void onPriceRemoved(double price) {
        priceLevels.remove(price);
    }

    @Override
    public void onChanged(OrdersPerSide ordersPerSide, double price) {
        boolean newLine = false;
        PriceLevel priceLevel = priceLevels.get(price);
        if (priceLevel == null) {
            onAdded(0, 0, price, 0, 0);
            newLine = true;
        }
        priceLevel = priceLevels.get(price);
        if (!priceLevel.onChange(ordersPerSide.getId(), ordersPerSide.getPriceToOrderLevelsMap().get(price))) {
            onPriceRemoved(price);
            fireLevelRemoved(price);
        } else {
            if (newLine) {
                fireLevelAdded(price, priceLevel);
            } else {
                fireLevelUpdated(price, priceLevel);
            }
        }
    }

    private void fireLevelAdded(double price, PriceLevel priceLevel) {
        listeners.forEach((listener) -> {
            listener.onPriceLevelAdded(price, priceLevel);
        });
    }

    private void fireLevelRemoved(double price) {
        listeners.forEach((listener) -> {
            listener.onPriceLevelRemoved(price);
        });
    }

    private void fireLevelUpdated(double price, PriceLevel priceLevel) {
        listeners.forEach((listener) -> {
            listener.onPriceLevelUpdated(price, priceLevel);
        });
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("OrderBook{" + name + "}").append("\n");
        buffer.append("------------------------------------------------------------------").append("\n");
        buffer.append(
                String.format(
                        "| %1$10s | %2$10s | %3$10s | %4$10s | %5$10s |",
                        "TotalOrds",
                        "TotalVol",
                        "Price",
                        "TotalVol",
                        "TotalOrds"
                )
        ).append("\n");
        buffer.append("------------------------------------------------------------------").append("\n");

        for (Map.Entry<Double, PriceLevel> entry : priceLevels.descendingMap().entrySet()) {
            OrdersPerLevel sellLevel = entry.getValue().getOrdersPerLevels()[0];
            OrdersPerLevel buyLevel = entry.getValue().getOrdersPerLevels()[1];
            if (sellLevel != null && sellLevel.getOrdersCount() != 0) {
                buffer.append(
                        String.format(
                                "| %1$10d | %2$10d | %3$10f ",
                                sellLevel.getOrdersCount(),
                                sellLevel.getAccumulatedVolume(),
                                entry.getKey()
                        )
                );
                if (buyLevel != null && buyLevel.getOrdersCount() != 0) {
                    buffer.append(
                            String.format(
                                    "| %1$-10d | %2$-10d |",
                                    buyLevel.getAccumulatedVolume(),
                                    buyLevel.getOrdersCount()
                            )
                    );
                } else {
                    buffer.append(
                            String.format(
                                    "| %1$10s | %2$10s |",
                                    "",
                                    ""
                            )
                    );
                }
                buffer.append("\n");
                buffer.append("------------------------------------------------------------------").append("\n");
            } else {
                if (buyLevel != null && buyLevel.getOrdersCount() != 0) {
                    buffer.append(
                            String.format(
                                    "| %1$10s | %2$10s | %3$10f ",
                                    "",
                                    "",
                                    entry.getKey()
                            )
                    );
                    if (buyLevel != null) {
                        buffer.append(
                                String.format(
                                        "| %1$-10d | %2$-10d |",
                                        buyLevel.getAccumulatedVolume(),
                                        buyLevel.getOrdersCount()
                                )
                        );
                        buffer.append("\n");
                    }
                    buffer.append("------------------------------------------------------------------").append("\n");
                }
            }
        }

        return buffer.toString();
    }
}
