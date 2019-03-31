package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.OrderEntry;
import org.fsin.matomat.database.model.ProductCountEntry;
import org.fsin.matomat.database.model.TransactionEntry;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ProductCountDAO {

    JdbcTemplate template;

    public ProductCountDAO(JdbcTemplate template) {
        this.template = template;
    }

    public List<ProductCountEntry> getByPurchase(TransactionEntry purchaseEntry) {

    }

    public List<ProductCountEntry> getByOrder(OrderEntry orderEntry) {

    }

}
