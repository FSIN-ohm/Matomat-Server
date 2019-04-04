package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class TransactionDAO {

    public JdbcTemplate template;

    public TransactionDAO(JdbcTemplate template) {
        this.template = template;
    }

    private static final RowMapper<TransactionEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        TransactionEntry entry = new TransactionEntry();
        entry.setId(rs.getInt("id"));
        entry.setDate(rs.getDate("date"));
        entry.setSenderId(rs.getInt("sender"));
        entry.setRecipientId(rs.getInt("recipient"));
        entry.setAmount(rs.getBigDecimal("amount"));
        return entry;
    };

    public List<TransactionEntry> getAll() throws DataAccessException {
        return template.query("select * from transactions_total", rowMapper);
    }

    public List<TransactionEntry> getBySender(UserEntry sender) throws DataAccessException {
        return template.query("select * from transactions_total where sender = ?", rowMapper, sender.getId());
    }

    public List<TransactionEntry> getByRecipient(UserEntry recipient) throws DataAccessException {
        return template.query("select * from transactions_total where recipient = ?", rowMapper, recipient.getId());
    }

    // ===============================================
    // =============== ADD TRANSACTIONS ==============
    // ===============================================

    public void addDeposit(TransactionEntry deposit) throws DataAccessException {
        template.update("call transaction_deposit(?, ?)", deposit.getRecipientId(), deposit.getAmount());
    }

    public void addWithdraw(TransactionEntry withdraw) throws  DataAccessException {
        template.update("call transaction_withdraw(?, ?)", withdraw.getSenderId(), withdraw.getAmount());

    }

    public void addTransfer(TransactionEntry transfer) throws DataAccessException {
        template.update("call ADD_TRANSFER(?, ?, ?)",
                transfer.getSenderId(),
                transfer.getRecipientId(),
                transfer.getAmount());
    }

    public void addOrder(OrderEntry orderEntry, List<ProductCountEntry> products) {
        int transactionId = template.update(
                "call transaction_order(?, ?)",
                orderEntry.getAdmin_id(), orderEntry.getCost()
        );

        String sql = "INSERT INTO ordered_products(order_transaction_id, product_detail_id, count) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = template.getDataSource().getConnection().prepareStatement(sql);
            for (ProductCountEntry product : products) {
                ps.setInt(1, transactionId);
                ps.setInt(2, product.getProductsId());
                ps.setInt(3, product.getCount());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void addPurchase(TransactionEntry purchaseEntry, List<ProductCountEntry> products) throws DataAccessException {
        int transactionId = template.queryForObject("call transaction_purchase(?)", int.class,
                purchaseEntry.getSenderId());

        String sql = "INSERT INTO purchase_amount_products(transaction_id, products_id, count) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = template.getDataSource().getConnection().prepareStatement(sql);
            for (ProductCountEntry product : products) {
                ps.setInt(1, transactionId);
                ps.setInt(2, product.getProductsId());
                ps.setInt(3, product.getCount());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e){
            System.out.println("inserting products failed");
            e.printStackTrace();
        }
    }

    // ===============================================
    // ==================== UTILS ====================
    // ===============================================

    public static RowMapper<TransactionEntry> getRowMapper() {
        return rowMapper;
    }
}