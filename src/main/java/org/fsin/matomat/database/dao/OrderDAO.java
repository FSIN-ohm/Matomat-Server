package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.OrderEntry;
import org.fsin.matomat.database.model.OrderedProductEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class OrderDAO {
    JdbcTemplate template;

    public OrderDAO(JdbcTemplate template) {
        this.template = template;
    }

    private static final RowMapper<OrderEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        OrderEntry entry = new OrderEntry();
        entry.setId(rs.getInt("id"));
        entry.setDate(rs.getDate("date"));
        entry.setSenderId(rs.getInt("sender"));
        entry.setRecipientId(rs.getInt("recipient"));
        entry.setAmount(rs.getBigDecimal("amount"));
        entry.setAdminId(rs.getInt("admin_id"));
        entry.setType(TransactionDAO.getTypeFromString(rs.getString("type")));
        return entry;
    };

    private static final RowMapper<OrderedProductEntry> orderedProductsRowMapper = (ResultSet rs, int rowNum) -> {
        OrderedProductEntry entry = new OrderedProductEntry();
        entry.setCount(rs.getInt("count"));
        entry.setProductId(rs.getInt("product_detail_id"));
        return entry;
    };

    public OrderEntry getOrder(long id) {
        return template.queryForObject("select * from order_total where id = ?", rowMapper, id);
    }

    public List<OrderedProductEntry> getOrderedProducts(OrderEntry order) {
        return template.query("select * from ordered_products where order_transaction_id = ?", orderedProductsRowMapper, order.getId());
    }

    public List<OrderedProductEntry> getAverageOfProductPerOrder() {
        return template.query("SELECT product_id, AVG(count) as count FROM ordered_products GROUP BY product_id;", orderedProductsRowMapper);
    }
}
