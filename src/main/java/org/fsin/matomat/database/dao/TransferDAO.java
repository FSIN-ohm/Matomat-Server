package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.TransferEntry;
import org.fsin.matomat.database.model.UserEntry;
import org.springframework.dao.DataAccessException;
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
        entry.setId(rs.getInt("ID"));
        entry.setDate(rs.getDate("Date"));
        entry.setSenderId(rs.getInt("sender"));
        entry.setRecipientId(rs.getInt("recipient"));
        entry.setCharged_amount(rs.getBigDecimal("charged_amount"));
        return entry;
    };

    public List<TransferEntry> getAll() throws DataAccessException {
        return template.query("select * from transfer_transactions", rowMapper);
    }

    public List<TransferEntry> getBySender(UserEntry sender) throws DataAccessException {
        return template.query("select * from transfer_transactions where sender = ?", rowMapper, sender.getId());
    }

    public List<TransferEntry> getByRecipient(UserEntry recipient) throws DataAccessException {
        return template.query("select * from transfer_transactions where recipient = ?", rowMapper, recipient.getId());
    }

    public void addTransfare(TransferEntry transfer) throws DataAccessException {
        template.update("call ADD_TRANSFER(?, ?, ?)",
                transfer.getSenderId(),
                transfer.getRecipientId(),
                transfer.getCharged_amount());
    }
}
