package org.trading.orderbook.gui.model;

import org.trading.orderbook.util.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The backing data model for a JTable's model.
 * It maintains an association between prices and levels, in a dynamic way and it does that because a JTable's data model
 * works with indexes and our books work with prices.
 * The main operations on this class are add, remove and update. These operations get translated in index based operations
 */
public class TableDataModel {

    private final List<Object[]> levelsList;

    private final List<Double> prices;

    public TableDataModel() {
        prices = new LinkedList<Double>();
        levelsList = new LinkedList<Object[]>();
    }

    public int size() {
        return levelsList.size();
    }

    public Object get(int index, int columnIndex) {
        return levelsList.get(index)[columnIndex];
    }

    public boolean add(double price, int sellOrdCnt, int sellAccVol, int buyAccVol, int buyOrdCnt) {
        int idx = indexForPrice(price);
        if (idx == -1) {
            int newIndex = potentialIndexForPrice(price);
            add(newIndex, price);
            add(newIndex, sellOrdCnt, sellAccVol, price, buyAccVol, buyOrdCnt);
            return true;
        }
        return false;
    }

    public boolean remove(double price) {
        int idx = indexForPrice(price);
        if (idx != -1) {
            removePrice(idx);
            removeLevel(idx);
            return true;
        }
        return false;
    }

    public boolean update(double price, int sellOrdCnt, int sellAccVol, int buyAccVol, int buyOrdCnt) {
        int idx = indexForPrice(price);
        if (idx != -1) {
            levelsList.get(idx)[0] = sellOrdCnt;
            levelsList.get(idx)[1] = sellAccVol;
            levelsList.get(idx)[2] = price;
            levelsList.get(idx)[3] = buyAccVol;
            levelsList.get(idx)[4] = buyOrdCnt;
            return true;
        }
        return false;
    }

    public int getSellOrdCnt(double price) {
        int idx = indexForPrice(price);
        return (Integer) get(idx, 0);
    }

    public int getSellAccVol(double price) {
        int idx = indexForPrice(price);
        return (Integer) get(idx, 1);
    }

    public int getBuyAccVol(double price) {
        int idx = indexForPrice(price);
        return (Integer) get(idx, 3);
    }

    public int getBuyOrdCnt(double price) {
        int idx = indexForPrice(price);
        return (Integer) get(idx, 4);
    }

    public Double[] getLevelPrices() {
        List<Double> doubles = new ArrayList<Double>();
        for (Object[] level : levelsList) {
            doubles.add((Double) level[2]);
        }
        return doubles.toArray(new Double[0]);
    }

    public Double[] getPrices() {
        return prices.toArray(new Double[0]);
    }

    private void add(int newIndex, double price) {
        prices.add(newIndex, price);
    }

    private void add(int newIndex, int sellOrdCnt, int sellAccVol, double price, int buyAccVol, int buyOrdCnt) {
        Object[] element = new Object[]{sellOrdCnt, sellAccVol, price, buyAccVol, buyOrdCnt};
        levelsList.add(newIndex, element);
    }

    private void removePrice(int index) {
        prices.remove(index);
    }

    private void removeLevel(int index) {
        levelsList.remove(index);
    }

    private int indexForPrice(double price) {
        return Utils.indexForPrice(price, prices);
    }

    private int potentialIndexForPrice(double price) {
        return Utils.potentialIndexForPrice(price, prices);
    }
}
