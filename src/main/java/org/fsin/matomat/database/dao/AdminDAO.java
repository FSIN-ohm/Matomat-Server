package org.fsin.matomat.database.dao;

import org.fsin.matomat.database.model.AdminEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class AdminDAO {
    JdbcTemplate template;

    public AdminDAO(JdbcTemplate template) {
        this.template = template;
    }

    private RowMapper<AdminEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        AdminEntry adminEntry = new AdminEntry();
        adminEntry.setId(rs.getInt("id"));
        adminEntry.setUsername(rs.getString("username"));
        adminEntry.setPassword(rs.getString("password"));
        adminEntry.setEmail(rs.getString("email"));
        adminEntry.setPassword(rs.getString("password_salt"));
        adminEntry.setCorespondingUserId(rs.getInt("user_id"));
        adminEntry.setAvailable(rs.getBoolean("available"));
        adminEntry.setBalance(rs.getInt("balance"));
        adminEntry.setLastSeen(rs.getTimestamp("last_seen"));
        return adminEntry;
    };

    public List<AdminEntry> getAll() throws DataAccessException {
        return template.query("select * from admin_balance", rowMapper);
    }

    public void addAdmin(AdminEntry admin) throws DataAccessException {
        template.update("call admin_create(?, ?, ?, ?)",
                admin.getUsername(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getPasswordSalt());
    }

    public void updateAdmin(AdminEntry admin) throws DataAccessException {
        template.update("call admin_update(?, ?, ?, ?, ?, ?)",
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getEmail(),
                admin.getPasswordSalt(),
                admin.isAvailable());
    }

    public AdminEntry getAdmin(int id) throws DataAccessException {
        return template.queryForObject("select * from admin_balance where id = ?", rowMapper, id);
    }

    public AdminEntry getAdmin(String username) throws DataAccessException {
        try {
            return template.queryForObject("select * from admin_balance where username = ?",
                    rowMapper,
                    username);
        } catch (Exception e){
            return null;
        }
    }
}
