package org.fsin.matomat.database;


public class DBAddPurchaseException extends RuntimeException {
    public DBAddPurchaseException(Exception e) {
        super(e);
    }
}
