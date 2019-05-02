package org.fsin.matomat.database.dao;

import org.apache.commons.codec.binary.Hex;
import org.fsin.matomat.database.model.UserEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class UsersDAO {
    JdbcTemplate template;
    public UsersDAO(JdbcTemplate template) {
        this.template = template;
    }

    private RowMapper<UserEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        UserEntry entry = new UserEntry();
        entry.setId(rs.getInt("id"));
        entry.setBalance(rs.getBigDecimal("balance"));
        entry.setLastSeen(rs.getTimestamp("last_seen"));
        entry.setAvailable(rs.getBoolean("available"));
        entry.setName(rs.getString("name"));
        entry.setAuthHash(rs.getBytes("auth_hash"));
        return entry;
    };

    public List<UserEntry> getAll() throws DataAccessException {
        return getAll(0, 1000000, false);
    }

    public List<UserEntry> getAll(int from, int to, boolean onlyAvailable) {
        if(onlyAvailable) {
            return template.query("select * from user_balance"
                    + " where ? <= id and id < ?"
                    + " and available = true", rowMapper, from, to);
        } else {
            return template.query("select * from user_balance where ? <= id and id < ?", rowMapper, from, to);
        }
    }

    public void addUser(UserEntry user) throws DataAccessException {
        System.out.println(Hex.encodeHexString(user.getAuthHash()));
        template.update("call user_create(?)", Hex.encodeHexString(user.getAuthHash()));
    }

    public void updateUser(UserEntry user) throws DataAccessException {
        template.update("call user_update(?, ?, ?, ?)",
                user.getId(),
                user.getAuthHash(),
                user.getName(),
                user.isAvailable());
    }

    public UserEntry getUser(int id) throws DataAccessException {
        return template.queryForObject("select * from user_balance where id = ?", rowMapper, id);
    }

    public UserEntry getUser(byte[] authHash) throws DataAccessException {
        byte[] data = new byte[20];
        System.arraycopy(authHash, 0, data, 0, authHash.length);
        return template.queryForObject("call user_balance_by_hash(unhex(?))", rowMapper, Hex.encodeHexString(data));
    }
}
