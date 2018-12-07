package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.TransferEntry;
import org.fsin.matomat.database.model.UserEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class TransferDAO {
    private JdbcTemplate template;

    public TransferDAO(JdbcTemplate template) {
        this.template = template;
    }

    RowMapper<TransferEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        TransferEntry entry = new TransferEntry();
        entry.setTransaction_id(rs.getInt("ID"));
        entry.setDate(rs.getDate("Date"));
        entry.setSender_id(rs.getInt("sender"));
        entry.setReceiver_id(rs.getInt("recipient"));
        entry.setCharched_amount(rs.getBigDecimal("charged_amount"));
        return entry;
    };

    public List<TransferEntry> getAll() {
        return template.query("select * from transfer_transactions", rowMapper);
    }

    public List<TransferEntry> getBySender(UserEntry sender) {
        return template.query("select * from transfer_transactions where sender = ?", rowMapper, sender.getId());
    }

    public List<TransferEntry> getByRecipient(UserEntry recipient) {
        return template.query("select * from transfer_transactions where recipient = ?", rowMapper, recipient.getId());
    }

    public void addTransfare(TransferEntry transfer) {
        template.update("call ADD_TRANSFER(?, ?, ?)",
                transfer.getSender_id(),
                transfer.getReceiver_id(),
                transfer.getCharched_amount());
    }
}
