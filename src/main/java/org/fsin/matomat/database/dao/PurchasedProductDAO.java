package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.ProductEntry;
import org.fsin.matomat.database.model.PurchaseEntry;
import org.fsin.matomat.database.model.PurchasedProductEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class PurchasedProductDAO {
    private JdbcTemplate template;

    public PurchasedProductDAO(JdbcTemplate template) {
        this.template = template;
    }

    private RowMapper<PurchasedProductEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        PurchasedProductEntry entry = new PurchasedProductEntry();
        entry.setProduct_id(rs.getInt("Product_ID"));
        entry.setTransaction_id(rs.getInt("Transaction_ID"));
        entry.setCount(rs.getInt("count"));
        return entry;
    };

    public List<PurchasedProductEntry> getAll() {
        return template.query("select * from Purchases", rowMapper);
    }

    public List<PurchasedProductEntry> getByPurchase(PurchaseEntry purchaseEntry) {
        return template.query("select * from Purchases where Transaction_ID = ?", rowMapper, purchaseEntry.getTransaction_id());
    }

    public List<PurchasedProductEntry> getByProduct(ProductEntry product) {
        return template.query("select * from Purchases where Product_ID = ?", rowMapper, product.getId());
    }
}