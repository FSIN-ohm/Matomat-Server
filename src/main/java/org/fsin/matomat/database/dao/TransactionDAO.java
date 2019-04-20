package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.*;
import org.fsin.matomat.rest.exceptions.BadRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class TransactionDAO {

    public static class UnknownTransactionTypeException extends RuntimeException {
        public UnknownTransactionTypeException(String message) {
            super(message);
        }
    }

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
        entry.setType(getTypeFromString(rs.getString("type")));
        return entry;
    };

    private static final RowMapper<OrderEntry> orderRowMapper = (ResultSet rs, int rowNum) -> {
        OrderEntry entry = new OrderEntry();
        entry.setId(rs.getInt("id"));
        entry.setDate(rs.getDate("date"));
        entry.setSenderId(rs.getInt("sender"));
        entry.setRecipientId(rs.getInt("recipient"));
        entry.setAmount(rs.getBigDecimal("amount"));
        entry.setAdminId(rs.getInt("admin_id"));
        entry.setType(getTypeFromString(rs.getString("type")));
        entry.setBuyCost(rs.getBigDecimal("buy_cost"));
        return entry;
    };

    public List<TransactionEntry> getAll(long from,
                                         long to,
                                         TransactionEntry.TransactionType type) throws DataAccessException {
        if(type == TransactionEntry.TransactionType.ANY)
            return template.query("select * from transactions_total"
                    + " where ? <= id and id < ?", rowMapper, from, to);
        return template.query("select * from transactions_total"
                + " where ? <= id and id < ?"
                + " and type = ?", rowMapper, from, to, type.value());
    }

    public List<TransactionEntry> getAll(long from,
                                         long to,
                                         TransactionEntry.TransactionType type,
                                         int user) throws DataAccessException {
        if(type == TransactionEntry.TransactionType.ANY)
            return template.query("select * from transactions_total"
                    + " where ? <= id and id < ?"
                    + " and sender = ?"
                    + " or recipient = ?", rowMapper, from, to, user, user);
        return template.query("select * from transactions_total"
                + " where ? <= id and id < ?"
                + " and where type = ?"
                + " and sender = ?"
                + " or recipient = ?", rowMapper, from, to, type, user);
    }

    public TransactionEntry getTransaction(Integer id) {
        return template.queryForObject("select * from transactions_total where id = ?;", rowMapper, id);
    }

    public OrderEntry getOrder(Integer id) {
        return template.queryForObject("select * from orders_total where id = ?;", orderRowMapper, id);
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
                orderEntry.getAdminId(), orderEntry.getBuyCost()
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

    /***************** UTILS **************************/

    public static TransactionEntry.TransactionType getTypeFromString(String type) {
        switch (type) {
            case "purchase": return TransactionEntry.TransactionType.PURCHASE;
            case "order": return TransactionEntry.TransactionType.ORDER;
            case "deposit": return TransactionEntry.TransactionType.DEPOSIT;
            case "withdraw": return TransactionEntry.TransactionType.WITHDRAW;
            case "transfere": return TransactionEntry.TransactionType.TRANSFERE;
            case "": return TransactionEntry.TransactionType.ANY;
            default: throw new UnknownTransactionTypeException("Transactin type not known: " + type);
        }
    }
}