package org.trading.orderbook.gui;


import org.trading.orderbook.gui.model.OrderBookModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class OrderBookFrame extends JFrame {

    private final JTable table;

    private final JPanel panel;

    public OrderBookFrame(String bookName, OrderBookModel model) {
        super(bookName);
        panel = new JPanel(new BorderLayout());
        table = new JTable(model);
        customiseTable();
        panel.add(new JScrollPane(table));
        add(panel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
    }

    private void customiseTable() {
        table.getTableHeader().setBackground(Color.ORANGE);
        table.getColumnModel().getColumn(0).setCellRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        setForeground(Color.red);
                        return this;
                    }
                }
        );
        table.getColumnModel().getColumn(1).setCellRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        setForeground(Color.red);
                        return this;
                    }
                }
        );
        table.getColumnModel().getColumn(2).setCellRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        setBackground(Color.LIGHT_GRAY);
                        setForeground(Color.BLACK);
                        return this;
                    }
                }
        );
        table.getColumnModel().getColumn(3).setCellRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        setForeground(Color.blue);
                        return this;
                    }
                }
        );
        table.getColumnModel().getColumn(4).setCellRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        setForeground(Color.blue);
                        return this;
                    }
                }
        );
    }
}
