package org.trading.orderbook.model;

import java.util.List;

public interface IdContainer extends IdObserver {

    public boolean contains(long orderId);

    public List<Long> getOrderIds();
}
