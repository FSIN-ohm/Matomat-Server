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
        entry.setId(rs.getInt("prod_id"));
        entry.setPrice(rs.getBigDecimal("price"));
        entry.setName(rs.getString("name"));
        entry.setImageUrl(rs.getString("image_url"));
        entry.setReorderPoint(rs.getInt("reorder_point"));
        entry.setBarcode(rs.getString("barcode"));
        entry.setAvailable(rs.getBoolean("available"));
        entry.setItemsPerCrate(rs.getInt("items_per_crate"));
        return entry;
    };

    public List<ProductEntry> getAll() throws DataAccessException {
        return template.query("SELECT * FROM products_all", rowMapper);
    }

    public List<ProductEntry> getActive() throws DataAccessException {
        return template.query("SELECT * FROM products_all WHERE available = 1", rowMapper);
    }

    public ProductEntry getDetail(int id)throws DataAccessException {
        return template.queryForObject("SELECT * FROM products_current where id = ?", rowMapper, id);
    }

    public void addProduct(ProductEntry product) throws DataAccessException {
        template.update("call product_add(?, ?, ?, ?, ?, ?)",
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getReorderPoint(),
                product.getBarcode(),
                product.getItemsPerCrate()
        );
    }

    public void updateProduct(ProductEntry product) throws DataAccessException {
        template.update("call product_update(?, ?, ?, ?, ?, ?, ?)",
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getReorderPoint(),
                product.getBarcode(),
                product.getItemsPerCrate()
        );
    }

    public void updatePrice(ProductEntry product)throws DataAccessException {
        template.update("call product_update_price(?, ?)",
                product.getId(),
                product.getPrice()
        );
    }
}
