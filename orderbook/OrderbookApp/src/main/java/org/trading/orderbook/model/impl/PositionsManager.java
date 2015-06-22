package org.trading.orderbook.model.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trading.orderbook.model.IPositionsManager;
import org.trading.orderbook.model.IPositionListener;

import java.util.ArrayList;
import java.util.List;

public class PositionsManager implements IPositionsManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionsManager.class);

    private static final PositionsManager INSTANCE = new PositionsManager();

    public static PositionsManager getInstance() {
        return INSTANCE;
    }

    private volatile double quantity;

    private volatile double amount;

    private final List<IPositionListener> listeners;

    private PositionsManager() {
        listeners = new ArrayList<>();
    }

    public synchronized void register(IPositionListener listener) {
        this.listeners.add(listener);
    }

    public synchronized void onFill(boolean isBuy, double price, double quantity) {
        int direction = isBuy ? 1 : -1;
        this.quantity += direction * quantity;
        this.amount += direction * price * quantity;
        notifyListeners();
    }

    @Override
    public double getQuantity() {
        return quantity;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    private void notifyListeners() {
        for (IPositionListener listener : listeners) {
            listener.positionChanged(this.quantity, this.amount);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Notified listeners on position update: quantity=" + quantity + ", amaount=" + amount);
        }
    }
}
