package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.ProductEntry;
import org.fsin.matomat.database.model.PriceEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class PriceDAO {
    JdbcTemplate template;

    public PriceDAO(JdbcTemplate template) {
        this.template = template;
    }

    private RowMapper<PriceEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        PriceEntry entry = new PriceEntry();
        entry.setId(rs.getInt("id"));
        entry.setPrice(rs.getBigDecimal("price"));
        entry.setProductDetailId(rs.getInt("product_id"));
        entry.setValidFrom(rs.getTimestamp("valid_from"));
        return entry;
    };

    public List<PriceEntry> getAll() throws DataAccessException {
        return template.query("SELECT * FROM product_prices", rowMapper);
    }

    public PriceEntry getById(int id) throws DataAccessException {
        return template.queryForObject("SELECT * FROM product_prices where id = ?", rowMapper, id);
    }

    public int updatePrice(ProductEntry product, PriceEntry price) throws DataAccessException {
        return template.update("call product_update_price(?, ?)",
                product.getId(), price.getPrice());
    }
}