package org.fsin.matomat.database.model;

import java.math.BigDecimal;

public class PurchaseEntry {

    private int productsId;
    private int count;

    public int getProductsId() {
        return productsId;
    }

    public void setProductsId(int productsId) {
        this.productsId = productsId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
