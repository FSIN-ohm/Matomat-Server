package org.fsin.matomat.database.dao;

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
}