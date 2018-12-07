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
        entry.setId(rs.getInt("ID"));
        entry.setAuthHash(rs.getBytes("auth_hash"));
        entry.setBalance(rs.getInt("Balance"));
        entry.setLastSeen(rs.getDate("LastSeen"));
        entry.setAvialable(rs.getBoolean("available"));
        return entry;
    };

    public List<UserEntry> getAll() throws DataAccessException {
        return template.query("select * from Users", rowMapper);
    }

    public void addUser(UserEntry user) throws DataAccessException {
        template.update("call ADD_USER(?)",
                user.getAuthHash());
    }

    public UserEntry getUser(int id) throws DataAccessException {
        return template.queryForObject("select * from Users where ID = ?", rowMapper, id);
    }

    public UserEntry getUser(byte[] authHash) throws DataAccessException {
        return template.queryForObject("select * from Users where auth_hash = ?", rowMapper, authHash);
    }
}
