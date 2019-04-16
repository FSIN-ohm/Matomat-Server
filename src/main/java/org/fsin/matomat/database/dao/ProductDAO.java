package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.ProductDetailEntry;
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
        entry.setId(rs.getInt("id"));
        entry.setPrice(rs.getBigDecimal("price"));
        entry.setProductDetailId(rs.getInt("product_detail_id"));
        entry.setValidFrom(rs.getTimestamp("valid_from"));
        return entry;
    };

    public List<ProductEntry> getAll() throws DataAccessException {
        return template.query("SELECT * FROM products", rowMapper);
    }

    public List<ProductEntry> getAllActive() throws DataAccessException {
        return template.query("SELECT * FROM products_all where available = 1", rowMapper);
    }

    public ProductEntry getById(int id) throws DataAccessException {
        return template.queryForObject("SELECT * FROM products where id = ?", rowMapper, id);
    }

    public int addProduct(ProductEntry product, ProductDetailEntry productDetailEntry) throws DataAccessException {
        return template.update("call product_add(?, ?, ?, ?, ?, ?)",
                productDetailEntry.getName(),
                product.getPrice(),
                productDetailEntry.getImageUrl(),
                productDetailEntry.getReorderPoint(),
                productDetailEntry.getBarcode(),
                productDetailEntry.getItemsPerCrate());
    }

    public int updatePrice(ProductEntry product) throws DataAccessException {
        return template.update("call product_update_price(?, ?)",
                product.getId(), product.getPrice());
    }
}