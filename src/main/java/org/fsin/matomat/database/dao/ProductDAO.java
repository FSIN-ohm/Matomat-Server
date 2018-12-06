package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.ProductEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class ProductDAO {
    JdbcTemplate template;

    public ProductDAO(JdbcTemplate template) {
        this.template = template;
    }

    private RowMapper<ProductEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        ProductEntry entry = new ProductEntry();
        entry.setId(rs.getInt("ID"));
        entry.setPrice(rs.getInt("price"));
        entry.setName(rs.getString("name"));
        entry.setImageUrl(rs.getString("image_url"));
        entry.setReorderPoint(rs.getInt("reorder_point"));
        entry.setProductHash(rs.getBytes("product_hash"));
        entry.setAvailable(rs.getBoolean("available"));
        return entry;
    };

    public List<ProductEntry> getAll() {
        return template.query("SELECT * FROM virtual_product", rowMapper);
    }
}
