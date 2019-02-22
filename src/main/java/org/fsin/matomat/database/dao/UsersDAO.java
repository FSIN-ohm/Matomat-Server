package org.fsin.matomat.database.dao;

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
        entry.setAuthHash(rs.getBinaryStream("auth_hash").toString());
        entry.setBalance(rs.getInt("balance"));
        entry.setLastSeen(rs.getDate("last_seen"));
        //entry.setAvialable(rs.getBoolean("available"));
        return entry;
    };

    private RowMapper<UserEntry> rowMapperDetail = (ResultSet rs, int rowNum) -> {
        UserEntry entry = new UserEntry();
        entry.setId(rs.getInt("id"));
        entry.setBalance(rs.getInt("balance"));
        entry.setLastSeen(rs.getDate("last_seen"));
        //entry.setAvialable(rs.getBoolean("available"));
        return entry;
    };

    public List<UserEntry> getAll() throws DataAccessException {
        return template.query("select * from user_balance", rowMapperDetail);
    }

    public void addUser(UserEntry user) throws DataAccessException {
        template.update("call user_create(?)", user.getAuthHash());
    }

    public UserEntry getUser(int id) throws DataAccessException {
        return template.queryForObject("select * from users where id = ?", rowMapper, id);
    }

    public UserEntry getUser(String authHash) throws DataAccessException {
        return template.queryForObject("select * from users where auth_hash = unhex(?)", rowMapper, authHash);
    }
}
