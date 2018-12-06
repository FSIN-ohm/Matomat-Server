package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.PurchaseEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class PurchaseDAO {
    public JdbcTemplate template;

    private RowMapper<PurchaseEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        PurchaseEntry entry = new PurchaseEntry();
        entry.setTransaction_id(rs.getInt("ID"));
        entry.setDate(rs.getDate("Date"));
        entry.setSender_id(rs.getInt("sender"));
        entry.setRecipient_id(rs.getInt("recipient"));
        entry.setCharge_amount(rs.getBigDecimal("charged_amount"));
        return entry;
    };

    public PurchaseDAO(JdbcTemplate template) {
        this.template = template;
    }

    public List<PurchaseEntry> getAll() {
        return template.query("select * from virtual_purchases", rowMapper);
    }
}
