package org.fsin.matomat.database;

import org.fsin.matomat.database.model.ProductCountEntry;
import org.springframework.dao.DataAccessException;

import java.util.List;

public class DBAddPurchaseException extends Exception {
    private List<ProductCountEntry> productsThatCouldNotBeAdded;
    public DBAddPurchaseException(List<ProductCountEntry> errordProducts, String message, DataAccessException e) {
        super(message, e);
        productsThatCouldNotBeAdded = errordProducts;
    }

    public List<ProductCountEntry> getProductsThatCouldNotBeAdded() {
        return productsThatCouldNotBeAdded;
    }

    public void setProductsThatCouldNotBeAdded(List<ProductCountEntry> productsThatCouldNotBeAdded) {
        this.productsThatCouldNotBeAdded = productsThatCouldNotBeAdded;
    }
}
