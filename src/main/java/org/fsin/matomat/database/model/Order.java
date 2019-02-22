package org.fsin.matomat.database.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    TransactionEntry transaction;
    BigDecimal cost;
    int admin_id;
    List<PurchaseEntry> products;

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public TransactionEntry getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntry transaction) {
        this.transaction = transaction;
    }

    public List<PurchaseEntry> getProducts() {
        return products;
    }

    public void setProducts(List<PurchaseEntry> products) {
        this.products = products;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }
}
