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

    RowMapper<TransactionEntry> rowMapperTransaction = (ResultSet rs, int rowNum) -> {
        TransactionEntry entry = new TransactionEntry();
        entry.setId(rs.getInt("id"));
        entry.setDate(rs.getDate("date"));
        entry.setSenderId(rs.getInt("sender"));
        entry.setRecipientId(rs.getInt("recipient"));
        entry.setAmount(rs.getBigDecimal("amount"));
        return entry;
    };

    public List<TransactionEntry> getAll() throws DataAccessException {
        return template.query("select * from transactions_total", rowMapperTransaction);
    }

    public List<TransactionEntry> getBySender(UserEntry sender) throws DataAccessException {
        return template.query("select * from transactions_total where sender = ?", rowMapperTransaction, sender.getId());
    }

    public List<TransactionEntry> getByRecipient(UserEntry recipient) throws DataAccessException {
        return template.query("select * from transactions_total where recipient = ?", rowMapperTransaction, recipient.getId());
    }

    public void addTransfer(TransferEntry transfer) throws DataAccessException {
        template.update("call transaction_transfer(?, ?, ?)",
                transfer.getSenderId(),
                transfer.getRecipientId(),
                transfer.getCharged_amount());
    }

    public void addPurchase(Purchase purchase) throws DataAccessException {
        int transactionId = template.queryForObject("call transaction_purchase(?)", int.class,
                purchase.getSenderId());

        String sql = "INSERT INTO purchase_amount_products(transaction_id, products_id, count) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = template.getDataSource().getConnection().prepareStatement(sql);
            for (PurchaseEntry product : purchase.getProducts() ) {
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

    public void addDeposit(TransferEntry deposit) throws DataAccessException {
        template.update("call transaction_deposit(?, ?)", deposit.getRecipientId(), deposit.getCharged_amount());
    }

    public void addWithdraw(TransferEntry withdraw) throws  DataAccessException {
        template.update("call transaction_withdraw(?, ?)", withdraw.getSenderId(), withdraw.getCharged_amount());

    }

    public void addOrder(Order order) {
        int transactionId = template.update(
                "call transaction_order(?, ?)",
                order.getAdmin_id(), order.getCost()
        );

        String sql = "INSERT INTO ordered_products(order_transaction_id, product_detail_id, count) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = template.getDataSource().getConnection().prepareStatement(sql);
            for (PurchaseEntry product : order.getProducts() ) {
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
}