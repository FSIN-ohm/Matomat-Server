package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.DBAddPurchaseException;
import org.fsin.matomat.database.model.ProductEntry;
import org.fsin.matomat.database.model.PurchaseEntry;
import org.fsin.matomat.database.model.PurchasedProductEntry;
import org.fsin.matomat.database.model.UserEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.rmi.AccessException;
import java.sql.ResultSet;
import java.util.ArrayList;
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

    public List<PurchaseEntry> getAll() throws DataAccessException {
        return template.query("select * from virtual_purchases", rowMapper);
    }

    public List<PurchaseEntry> getBySender(UserEntry sender) throws DataAccessException {
        return template.query("select * from virtual_purchases where sender = ?", rowMapper, sender.getId());
    }

    public List<PurchaseEntry> getByRecipient(UserEntry recipient) throws DataAccessException {
        return template.query("select * from virtual_purchases where recipient = ?", rowMapper, recipient.getId());
    }

    public void addNewPurchase(PurchaseEntry purchase, List<PurchasedProductEntry> ppList) throws DataAccessException,
            DBAddPurchaseException {
        PurchasedProductEntry first = ppList.remove(0);
        Integer transaction_id =
                template.queryForObject("select ADD_PURCHASE(?,?,?,?)",
                        Integer.class,
                        purchase.getSender_id(),
                        purchase.getRecipient_id(),
                        first.getProduct_id(),
                        first.getCount());
        try {
            for (int i = 0; i < ppList.size(); i++) {
                PurchasedProductEntry entry = ppList.remove(i);
                template.update("CALL ADD_PURCHASE_TO_EXISTING_TRANSACTION(?, ?, ?)",
                        transaction_id,
                        entry.getProduct_id(),
                        entry.getCount());
            }
        } catch (DataAccessException e) {
            throw new DBAddPurchaseException(ppList, "Could buy several products", e);
        }
    }
}
