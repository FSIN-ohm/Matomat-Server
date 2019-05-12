package org.fsin.matomat.database.model;

public class OrderEntry extends TransactionEntry {
    int adminId;

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
