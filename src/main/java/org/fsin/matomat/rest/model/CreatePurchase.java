package org.fsin.matomat.rest.model;

public class CreatePurchase {
    private ProductAmount[] orders;

    public ProductAmount[] getOrders() {
        return orders;
    }

    public void setOrders(ProductAmount[] orders) {
        this.orders = orders;
    }
}
