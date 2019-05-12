package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.PriceEntry;
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
        entry.setImageUrl(rs.getString("image_url"));
        entry.setReorderPoint(rs.getInt("reorder_point"));
        entry.setAvailable(rs.getBoolean("available"));
        entry.setBarcode(rs.getString("barcode"));
        entry.setItemsPerCrate(rs.getInt("items_per_crate"));
        entry.setName(rs.getString("name"));
        entry.setStock(rs.getInt("stock"));
        entry.setPrice(rs.getBigDecimal("price"));
        entry.setValidDate(rs.getTimestamp("valid_from"));
        return entry;
    };

    public List<ProductEntry> getAll(boolean onlyAvailable) throws DataAccessException {
        if(onlyAvailable)
            return template.query("SELECT * from products_all where available = 1", rowMapper);
        return template.query("SELECT * from products", rowMapper);
    }

    public ProductEntry getById(int id) throws DataAccessException {
        return template.queryForObject("SELECT * from products_all where id = ?", rowMapper, id);
    }

    public int updateProduct(ProductEntry entry) throws DataAccessException {
        return template.update("call product_update(?, ?, ?, ?, ?, ?, ?)",
                entry.getId(),
                entry.getName(),
                entry.getImageUrl(),
                entry.getReorderPoint(),
                entry.isAvailable(),
                entry.getBarcode(),
                entry.getItemsPerCrate());
    }

    public int addProduct(ProductEntry productEntry, PriceEntry priceEntry) throws DataAccessException {
        return template.update("call product_add(?, ?, ?, ?, ?, ?)",
                productEntry.getName(),
                priceEntry.getPrice(),
                productEntry.getImageUrl(),
                productEntry.getReorderPoint(),
                productEntry.getBarcode(),
                productEntry.getItemsPerCrate());
    }

    public int updatePrice(ProductEntry product) throws DataAccessException {
        return template.update("call product_update_price(?, ?)",
                product.getId(), product.getPrice());
    }
}