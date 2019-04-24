package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class ProductCountDAO {

    JdbcTemplate template;

    public ProductCountDAO(JdbcTemplate template) {
        this.template = template;
    }

    private static final RowMapper<ProductCountEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        ProductCountEntry entry = new ProductCountEntry();
        entry.setCount(rs.getInt("count"));
        entry.setPriceId(rs.getInt("products_id"));
        return entry;
    };

    public List<ProductCountEntry> getByPurchase(TransactionEntry purchaseEntry) {
        return template.query("select * from purchase_amount_products where transaction_id = ?",
                rowMapper,
                purchaseEntry.getId());
    }
}
