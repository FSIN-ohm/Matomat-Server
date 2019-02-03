package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.ProductEntry;
import org.springframework.dao.DataAccessException;
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
        entry.setPrice(rs.getBigDecimal("price"));
        entry.setName(rs.getString("name"));
        entry.setImageUrl(rs.getString("image_url"));
        entry.setReorderPoint(rs.getInt("reorder_point"));
        entry.setProductHash(rs.getBytes("product_hash"));
        entry.setAvailable(rs.getBoolean("available"));
        return entry;
    };

    public List<ProductEntry> getAll() throws DataAccessException {
        return template.query("SELECT * FROM virtual_product", rowMapper);
    }

    public void addProduct(ProductEntry product) throws DataAccessException {
        template.update("call ADD_PRODUCT(?, ?, ?, ?, ?)",
                product.getPrice(),
                product.getName(),
                product.getImageUrl(),
                product.getReorderPoint(),
                product.getProductHash());
    }

    public void updateProduct(ProductEntry product) throws DataAccessException {
        template.update("call SET_PRODUCT(?, ?, ?, ?, ?)",
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getReorderPoint(),
                product.getProductHash());
    }
}
