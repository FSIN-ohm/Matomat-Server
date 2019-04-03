package org.fsin.matomat.database.model;

import java.math.BigDecimal;

public class OrderEntry extends TransactionEntry {
    BigDecimal buyCost;
    int adminId;

    public BigDecimal getBuyCost() {
        return buyCost;
    }

    public void setBuyCost(BigDecimal buyCost) {
        this.buyCost = buyCost;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
