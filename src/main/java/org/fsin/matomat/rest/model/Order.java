package org.fsin.matomat.rest.model;

public class Order extends Transaction {
    private int admin;
    private OrderedProduct purchased[];

    public Order() {

    }

    public Order(Transaction t) {
        super(t);
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public OrderedProduct[] getPurchased() {
        return purchased;
    }

    public void setPurchased(OrderedProduct[] purchased) {
        this.purchased = purchased;
    }
}
