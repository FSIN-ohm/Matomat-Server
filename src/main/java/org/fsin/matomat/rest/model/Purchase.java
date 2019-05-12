package org.fsin.matomat.rest.model;

public class Purchase extends Transaction {
    private ProductCountPrice[] purchased;

    public Purchase(Transaction t) {
        super(t);
    }

    public ProductCountPrice[] getPurchased() {
        return purchased;
    }

    public void setPurchased(ProductCountPrice[] purchased) {
        this.purchased = purchased;
    }
}
