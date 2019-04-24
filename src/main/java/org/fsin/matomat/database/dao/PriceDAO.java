package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.PriceEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.HashMap;
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
        entry.setProductId(rs.getInt("product_id"));
        entry.setValidFrom(rs.getTimestamp("valid_from"));
        return entry;
    };

    public List<PriceEntry> getAll() throws DataAccessException {
        return template.query("SELECT * FROM product_prices", rowMapper);
    }

    public List<PriceEntry> getActive() throws DataAccessException {
        return template.query("SELECT * from current_prices", rowMapper);
    }

    public HashMap<Integer, PriceEntry> getActiveAsProductIdHashMap() throws DataAccessException {
        List<PriceEntry> list = getActive();
        HashMap<Integer, PriceEntry> map = new HashMap<>(list.size());
        for(PriceEntry entry : list) {
            map.put(entry.getProductId(), entry);
        }
        return map;
    }

    public PriceEntry getById(int id) throws DataAccessException {
        return template.queryForObject("SELECT * FROM product_prices where id = ?", rowMapper, id);
    }
}