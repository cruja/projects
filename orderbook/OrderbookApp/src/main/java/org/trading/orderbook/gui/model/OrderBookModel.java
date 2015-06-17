package org.trading.orderbook.gui.model;

import org.trading.orderbook.model.IModelObserver;
import org.trading.orderbook.model.impl.side.OrdersPerSide;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * The backing table model used by a JTable component.
 * By being a model observer, it gets notified by changes within the order book.
 */
public class OrderBookModel extends AbstractTableModel implements IModelObserver {

    private final String[] header;

    private final TableDataModel dataModel;

    private final long refreshDelay;

    public OrderBookModel(long refreshDelay) {
        header = new String[]{"TotalOrders", "TotalVolume", "Price", "TotalVolume", "TotalOrders"};
        dataModel = new TableDataModel();
        this.refreshDelay = refreshDelay;
    }

    @Override
    public int getRowCount() {
        return dataModel.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Integer.class;
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public String getColumnName(int column) {
        return header[column];
    }

    /**
     * The JTable component will display price levels from top to bottom, the highest price at top and the lowest at bottom.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        int index = dataModel.size() - 1 - rowIndex;
        return dataModel.get(index, columnIndex);
    }

    @Override
    public void onAdded(final int sellOrdCnt, final int sellAccVol, final double price, final int buyAccVol, final int buyOrdCnt) {
        sleep();
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if (dataModel.add(price, sellOrdCnt, sellAccVol, buyAccVol, buyOrdCnt)) {
                            fireTableDataChanged();
                        }
                    }
                });
    }

    @Override
    public void onPriceRemoved(final double price) {
        sleep();
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if (dataModel.remove(price)) {
                            fireTableDataChanged();
                        }
                    }
                }
        );
    }

    @Override
    public void onUpdated(final int sellOrdCnt, final int sellAccVol, final double price, final int buyAccVol, final int buyOrdCnt) {
        sleep();
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if (dataModel.update(price, sellOrdCnt, sellAccVol, buyAccVol, buyOrdCnt)) {
                            fireTableDataChanged();
                        }
                    }
                }
        );
    }

    @Override
    public void onFilled(int volume) {

    }

    @Override
    public void onOrderDeleted(long orderId) {
    }

    @Override
    public void onUpdate(OrdersPerSide orderOrdersPerSide, double price) {

    }

    @Override
    public void onOrderAdded(long orderId, int size) {
    }

    @Override
    public void onChanged(OrdersPerSide ordersPerSide, double price) {
    }

    @Override
    public void onOrderFilled(long orderId, int volFilled) {
    }

    @Override
    public void onOrderPartiallyFilled(long orderId, int size, int volFilled, int volRemained) {
    }

    private void sleep() {
        try {
            Thread.sleep(refreshDelay);
        } catch (InterruptedException e) {
        }
    }
}
