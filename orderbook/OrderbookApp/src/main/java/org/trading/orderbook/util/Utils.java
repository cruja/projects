package org.trading.orderbook.util;


import java.util.List;

public class Utils {

    public static int indexForPrice(double price, List<Double> prices) {
        int idx = 0;
        if (prices.size() == 0) {
            return -1;
        }
        for (Double cPrice : prices) {
            if (price == cPrice) {
                break;
            }
            idx++;
        }
        if (idx >= prices.size()) {
            return -1;
        }
        return idx;
    }

    public static int potentialIndexForPrice(double price, List<Double> prices) {
        int idx = 0;
        for (Double cPrice : prices) {
            if (price < cPrice) {
                break;
            }
            idx++;
        }
        return idx;
    }
}
