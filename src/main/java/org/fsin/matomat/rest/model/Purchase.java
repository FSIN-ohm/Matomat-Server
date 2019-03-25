package org.fsin.matomat.rest.model;

public class Purchase extends Transaction {
    public Product[] products;

    public Product[] getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }
}
