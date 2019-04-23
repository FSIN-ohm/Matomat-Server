package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.ProductDetailEntry;
import org.fsin.matomat.database.model.ProductEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class ProductDetailDAO {
    JdbcTemplate template;

    public ProductDetailDAO(JdbcTemplate template) {
        this.template = template;
    }

    private RowMapper<ProductDetailEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        ProductDetailEntry entry = new ProductDetailEntry();
        entry.setId(rs.getInt("id"));
        entry.setImageUrl(rs.getString("image_url"));
        entry.setReorderPoint(rs.getInt("reorder_point"));
        entry.setAvailable(rs.getBoolean("available"));
        entry.setBarcode(rs.getString("barcode"));
        entry.setItemsPerCrate(rs.getInt("items_per_crate"));
        entry.setName(rs.getString("name"));
        return entry;
    };

    public List<ProductDetailEntry> getAll() throws DataAccessException {
        return template.query("SELECT * from product_detail", rowMapper);
    }

    public ProductDetailEntry getById(int id) throws DataAccessException {
        return template.queryForObject("SELECT * from product_detail where id = ?", rowMapper, id);
    }

    public int updateDetail(ProductDetailEntry entry) throws DataAccessException {
        return template.update("call product_update(?, ?, ?, ?, ?, ?, ?)",
                entry.getId(),
                entry.getName(),
                entry.getImageUrl(),
                entry.getReorderPoint(),
                entry.isAvailable(),
                entry.getBarcode(),
                entry.getItemsPerCrate());
    }
}