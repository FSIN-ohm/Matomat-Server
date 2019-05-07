package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.ProductStockEntry;
import org.fsin.matomat.database.model.UserEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class ProductStockDAO {
    JdbcTemplate template;

    public ProductStockDAO(JdbcTemplate template) {
        this.template = template;
    }

    RowMapper<ProductStockEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        ProductStockEntry entry = new ProductStockEntry();
        entry.setUser_id(rs.getInt("user_id"));
        entry.setProduct_id(rs.getInt("product_id"));
        entry.setSock(rs.getInt("stock"));
        return entry;
    };

    public List<ProductStockEntry> getAll() throws DataAccessException {
        return template.query("select * from product_stocks", rowMapper);
    }

    public List<ProductStockEntry> getStockForUser(UserEntry user) throws DataAccessException {
        return template.query("select * from product_stocks where user_id = ?", rowMapper, user.getId());
    }
}