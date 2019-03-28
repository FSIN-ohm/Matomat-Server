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
        byte[] authHash = new byte[20];
        try {
            int bytesReed = rs.getBinaryStream("auth_hash").read(authHash);
            if(bytesReed != 20) {
                throw new Exception("not engouth bytes read form user input: " + bytesReed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        entry.setBalance(rs.getInt("balance"));
        entry.setLastSeen(rs.getDate("last_seen"));
        entry.setAvailable(rs.getBoolean("available"));
        return entry;
    };

    private RowMapper<UserEntry> rowMapperDetail = (ResultSet rs, int rowNum) -> {
        UserEntry entry = new UserEntry();
        entry.setId(rs.getInt("id"));
        entry.setBalance(rs.getInt("balance"));
        entry.setLastSeen(rs.getDate("last_seen"));
        entry.setAvailable(rs.getBoolean("available"));
        return entry;
    };

    public List<UserEntry> getAll() throws DataAccessException {
        return template.query("select * from user_balance", rowMapperDetail);
    }

    public void addUser(UserEntry user) throws DataAccessException {
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
        return template.queryForObject("select * from users where id = ?", rowMapper, id);
    }

    public UserEntry getUser(byte[] authHash) throws DataAccessException {
        byte[] data = new byte[20];
        System.arraycopy(authHash, 0, data, 0, authHash.length);
        return template.queryForObject("select * from users where auth_hash = unhex(?)", rowMapper, Hex.encodeHexString(data));
    }
}
