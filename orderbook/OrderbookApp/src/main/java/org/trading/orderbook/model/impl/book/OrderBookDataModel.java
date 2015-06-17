package org.trading.orderbook.model.impl.book;

import org.trading.orderbook.model.IdContainer;
import org.trading.orderbook.model.impl.Order;
import org.trading.orderbook.model.impl.exception.OrderIdAlreadyExistsException;
import org.trading.orderbook.model.impl.side.SellOrdersPerSide;
import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.impl.DefaultModelObserver;
import org.trading.orderbook.model.impl.side.BuyOrdersPerSide;
import org.trading.orderbook.model.impl.side.OrdersPerSide;

import java.util.ArrayList;
import java.util.List;

public class OrderBookDataModel {

    // stores all SELL orders from the current order book
    private final OrdersPerSide sellOrdersPerSide;

    // stores all BUY orders from the current order book
    private final OrdersPerSide buyOrdersPerSide;

    // stores all order IDs currently in use
    private final IdContainer idContainer;

    // all components that will be notified on model changes
    private final List<IModelObserver> modelObservers;

    private final boolean isLogging;

    private final IModelObserver observer = new DefaultModelObserver() {

        @Override
        public void onOrderFilled(long orderId, int volFilled) {
            fireOrderFilled(orderId, volFilled);
            if (isLogging) {
                System.out.println("Order " + orderId + " filled: " + volFilled + "@" + volFilled);
            }
        }

        @Override
        public void onOrderPartiallyFilled(long orderId, int size, int volFilled, int volRemained) {
            fireOrderPartiallyFilled(orderId, size, volFilled, volRemained);
            if (isLogging) {
                System.out.println("Order " + orderId + " partially filled: " + volFilled + "@" + volRemained);
            }
        }

        @Override
        public void onOrderDeleted(long orderId) {
            fireIdDeleted(orderId);
            if (isLogging) {
                System.out.println("Order " + orderId + " deleted");
            }
        }

        @Override
        public void onUpdate(OrdersPerSide orderOrdersPerSide, double price) {
            fireChanged(orderOrdersPerSide, price);
        }
    };

    public OrderBookDataModel(IdContainer idContainer, boolean isLogging) {
        this.isLogging = isLogging;
        this.idContainer = idContainer;
        modelObservers = new ArrayList<IModelObserver>();
        sellOrdersPerSide = new SellOrdersPerSide(observer);
        buyOrdersPerSide = new BuyOrdersPerSide(observer);
        //register(observer);
    }

    public void add(Order order) {
        if (idContainer.contains(order.getOrderId())) {
            throw new OrderIdAlreadyExistsException("For order id: " + order.getOrderId());
        }
        OrdersPerSide orderOrdersPerSide = getSide(order.isBuy());
        OrdersPerSide otherOrdersPerSide = getSide(!order.isBuy());

        int otherSideAccumulatedVolume = otherOrdersPerSide.getAccumulatedVolume();
        boolean otherSideMatch = otherOrdersPerSide.isMatch(order.getPrice());
        if (!(otherSideAccumulatedVolume > 0) || !otherSideMatch) {
            orderOrdersPerSide.add(order);
            fireIdAdded(order.getOrderId(), order.getSize());
            fireChanged(orderOrdersPerSide, order.getPrice());
        } else {
            fill(order, otherOrdersPerSide);
            if (order.getVolume() > 0) {
                orderOrdersPerSide.add(order);
                fireIdAdded(order.getOrderId(), order.getSize());
                fireChanged(orderOrdersPerSide, order.getPrice());
            } else {
                fireOrderDeleted(order.getOrderId());
            }
        }
    }

    public void delete(Order order) {
        if (!idContainer.contains(order.getOrderId())) {
            if (isLogging) {
                System.out.println("WARN - no such order id to be deleted: " + order.getOrderId());
            }
            return;
        }
        OrdersPerSide orderOrdersPerSide = getSide(order.isBuy());
        orderOrdersPerSide.delete(order);
        fireOrderDeleted(order.getOrderId());
        fireChanged(orderOrdersPerSide, order.getPrice());
    }

    public void cancel(Order order) {
        if (!idContainer.contains(order.getOrderId())) {
            if (isLogging) {
                System.out.println("WARN - no such order id to be deleted: " + order.getOrderId());
            }
            return;
        }
        OrdersPerSide orderOrdersPerSide = getSide(order.isBuy());
        orderOrdersPerSide.delete(order);
        fireOrderDeleted(order.getOrderId());
        fireChanged(orderOrdersPerSide, order.getPrice());
    }

    public void register(IModelObserver modelObserver) {
        modelObservers.add(modelObserver);
    }

    private Order fill(Order order, OrdersPerSide otherOrderOrdersPerSide) {
        if (isLogging) {
            System.out.println("Order " + order.getOrderId() + ": filling volume " + order.getVolume());
        }
        if (order.getVolume() >= otherOrderOrdersPerSide.getAccumulatedVolume()) {
            int filled = otherOrderOrdersPerSide.getAccumulatedVolume();
            order.setVolume(order.getVolume() - filled);
            otherOrderOrdersPerSide.fillAll();
            if (order.getVolume() > 0) {
                fireOrderPartiallyFilled(order.getOrderId(), order.getSize(), filled, order.getVolume());
            } else {
                fireOrderFilled(order.getOrderId(), filled);
            }
            fireFilled(filled);
        } else {
            int filled = otherOrderOrdersPerSide.fill(order.getVolume(), order.getPrice());
            assert order.getVolume() - filled == 0;
            order.setVolume(order.getVolume() - filled);
            fireOrderFilled(order.getOrderId(), filled);
            fireFilled(filled);
        }
        return order;
    }

    private OrdersPerSide getSide(boolean isBuySide) {
        return isBuySide ? buyOrdersPerSide : sellOrdersPerSide;
    }

    private void fireChanged(OrdersPerSide ordersPerSide, double price) {
        for (IModelObserver observer : modelObservers) {
            observer.onChanged(ordersPerSide, price);
        }
    }

    private void fireIdAdded(long orderId, int size) {
        idContainer.onAdded(orderId);
        for (IModelObserver observer : modelObservers) {
            observer.onOrderAdded(orderId, size);
        }
        if (isLogging) {
            System.out.println("Order " + orderId + " added");
        }
    }

    private void fireIdDeleted(long orderId) {
        idContainer.onDeleted(orderId);
    }

    private void fireOrderDeleted(long orderId) {
        for (IModelObserver observer : modelObservers) {
            observer.onOrderDeleted(orderId);
        }
    }

    private void fireOrderFilled(long orderId, int volume) {
        for (IModelObserver observer : modelObservers) {
            observer.onOrderFilled(orderId, volume);
        }
    }

    private void fireOrderPartiallyFilled(long orderId, int size, int volFilled, int volRemained) {
        for (IModelObserver observer : modelObservers) {
            observer.onOrderPartiallyFilled(orderId, size, volFilled, volRemained);
        }
    }

    private void fireFilled(int volume) {
        for (IModelObserver observer : modelObservers) {
            observer.onFilled(volume);
        }
        if (isLogging) {
            System.out.println("Volume filled: " + volume);
        }
    }

    // for test purposes
    public double getLowestPrice(boolean isBuySide) {
        return isBuySide ? buyOrdersPerSide.getLowestPrice() : sellOrdersPerSide.getLowestPrice();
    }

    // for test purposes
    public double getHighestPrice(boolean isBuySide) {
        return isBuySide ? buyOrdersPerSide.getHighestPrice() : sellOrdersPerSide.getHighestPrice();
    }

    // for test purposes
    public double getBestPrice(boolean isBuySide) {
        return isBuySide ? buyOrdersPerSide.getBestPrice() : sellOrdersPerSide.getBestPrice();
    }

    // for test purposes
    public int getAccVol(boolean isBuySide) {
        return isBuySide ? buyOrdersPerSide.getAccumulatedVolume() : sellOrdersPerSide.getAccumulatedVolume();
    }

    // for test purposes
    public int getOrdersCount(boolean isBuySide) {
        return isBuySide ? buyOrdersPerSide.getOrdersCount() : sellOrdersPerSide.getOrdersCount();
    }

    // for test purposes
    public int getOrdersCount(double price, boolean isBuySide) {
        return isBuySide ? buyOrdersPerSide.getOrdersCount(price) : sellOrdersPerSide.getOrdersCount(price);
    }

    // for test purposes
    public int getAccVolume(double price, boolean isBuySide) {
        return isBuySide ? buyOrdersPerSide.getAccumulatedVolume(price) : sellOrdersPerSide.getAccumulatedVolume(price);
    }
}
