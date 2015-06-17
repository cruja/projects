package org.trading.orderbook.model;

public class Order {

    private int id;

    private String orderBook;

    private Side side;

    private int size;

    private double price;

    private int filled;

    private OrderState orderState;

    public Order() {
        orderState = OrderState.CREATED;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(String orderBook) {
        this.orderBook = orderBook;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFilled() {
        return filled;
    }

    public void setFilled(int filled) {
        this.filled = filled;
    }

    public boolean fill(int delta) {
        if (filled < size) {
            this.filled += delta;
            return true;
        }
        return false;
    }

    public String serialize(String action) {
        return "{" + action + ";" + id + ";" + orderBook + ";" + side + ";" + size + ";" + price + "}";
    }

    @Override
    public String toString() {
        return "{" + id + ";" + orderBook + ";" + side + ";" + size + ";" + price + "}";
    }

}
