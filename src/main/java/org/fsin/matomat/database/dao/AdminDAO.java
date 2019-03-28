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
        adminEntry.setPassword_salt(rs.getString("password_salt"));
        adminEntry.setCorespondingUser_id(rs.getInt("user_id"));
        return adminEntry;
    };

    public List<AdminEntry> getAll() throws DataAccessException {
        return template.query("select * from admins", rowMapper);
    }

    public void addAdmin(AdminEntry admin) throws DataAccessException {
        template.update("call admin_create(?, ?, ?, ?)",
                admin.getUsername(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getPassword_salt()
                );
    }

    public void updateAdmin(AdminEntry admin) throws DataAccessException {
        template.update("call admin_update(?, ?, ?, ?, ?)",
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getEmail(),
                admin.getPassword_salt());
    }

    public AdminEntry getAdmin(int id) throws DataAccessException {
        return template.queryForObject("select * from admins where id = ?", rowMapper, id);
    }

    public AdminEntry getAdmin(String username) throws DataAccessException {
        try {
            return template.queryForObject("select * from admins where username = ?",
                    rowMapper,
                    username);
        } catch (Exception e){
            return null;
        }
    }
}
