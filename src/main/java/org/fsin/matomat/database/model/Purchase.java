package org.fsin.matomat.database.model;

import java.util.List;

public class Purchase extends TransactionEntry {
    List<PurchaseEntry> products;

    public List<PurchaseEntry> getProducts() {
        return products;
    }

    public void setProducts(List<PurchaseEntry> products) {
        this.products = products;
    }
}
