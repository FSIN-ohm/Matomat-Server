package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.ProductBoughtEntry;
import org.fsin.matomat.database.model.TransactionEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class ProductBoughtDAO {

    JdbcTemplate template;

    private RowMapper<ProductBoughtEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        ProductBoughtEntry entry = new ProductBoughtEntry();
        entry.setCount(rs.getInt("count"));
        entry.setTransactionId(rs.getInt("transaction_id"));
        entry.setProductId(rs.getInt("product_id"));
        entry.setUnitPrice(rs.getBigDecimal("unit_price"));
        return entry;
    };

    public ProductBoughtDAO(JdbcTemplate template) {
        this.template = template;
    }

    public List<ProductBoughtEntry> getByPurchase(TransactionEntry purchase) {
        return template.query("select * from products_bought where transaction_id = ?", rowMapper, purchase.getId());
    }
}
