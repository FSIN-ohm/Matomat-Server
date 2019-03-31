package org.fsin.matomat.database.model;

import java.math.BigDecimal;

public class OrderEntry extends TransactionEntry {
    BigDecimal cost;
    int admin_id;

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }
}
