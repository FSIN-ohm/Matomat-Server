package org.fsin.matomat.database;

import org.fsin.matomat.database.model.ProductStockEntry;
import org.fsin.matomat.database.model.PurchasedProductEntry;
import org.springframework.dao.DataAccessException;

import java.util.List;

public class DBAddPurchaseException extends Exception {
    private List<PurchasedProductEntry> productsThatCouldNotBeAdded;
    public DBAddPurchaseException(List<PurchasedProductEntry> errordProducts, String message, DataAccessException e) {
        super(message, e);
        productsThatCouldNotBeAdded = errordProducts;
    }

    public List<PurchasedProductEntry> getProductsThatCouldNotBeAdded() {
        return productsThatCouldNotBeAdded;
    }

    public void setProductsThatCouldNotBeAdded(List<PurchasedProductEntry> productsThatCouldNotBeAdded) {
        this.productsThatCouldNotBeAdded = productsThatCouldNotBeAdded;
    }
}
