package org.fsin.matomat.rest.model;

public class Order extends Transaction {
    private Product[] products;
    private long buy_cost;

    public Product[] getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }

    public long getBuy_cost() {
        return buy_cost;
    }

    public void setBuy_cost(long buy_cost) {
        this.buy_cost = buy_cost;
    }
}
