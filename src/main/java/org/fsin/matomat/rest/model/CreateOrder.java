package org.fsin.matomat.rest.model;


public class CreateOrder {
    private int amount;
    private OrderedProduct[] orders;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public OrderedProduct[] getOrders() {
        return orders;
    }

    public void setOrders(OrderedProduct[] orders) {
        this.orders = orders;
    }
}
