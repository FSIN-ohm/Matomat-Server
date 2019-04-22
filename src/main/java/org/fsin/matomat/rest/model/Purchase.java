package org.fsin.matomat.rest.model;

public class Purchase extends Transaction {
    private ProductAmount[] products;

    public Purchase(Transaction t) {
        super(t);
    }

    public ProductAmount[] getProducts() {
        return products;
    }

    public void setProducts(ProductAmount[] products) {
        this.products = products;
    }
}
