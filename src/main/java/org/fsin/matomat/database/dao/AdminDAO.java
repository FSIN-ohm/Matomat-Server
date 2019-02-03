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
        adminEntry.setId(rs.getInt("ID"));
        adminEntry.setUsername(rs.getString("username"));
        adminEntry.setPassword(rs.getBytes("password"));
        adminEntry.setEmail(rs.getString("email"));
        adminEntry.setPassword_salt(rs.getBytes("password_salt"));
        adminEntry.setCorespondingUser_id(rs.getInt("User_ID"));
        return adminEntry;
    };

    public List<AdminEntry> getAll() throws DataAccessException {
        return template.query("select * from Admins", rowMapper);
    }

    public void addAdmin(AdminEntry admin) throws DataAccessException {
        template.update("call ADD_ADMIN(?, ? ?)",
                admin.getUsername(),
                admin.getPassword(),
                admin.getEmail());
    }

    public void updateAdmin(AdminEntry admin) throws DataAccessException {
        template.update("call SET_ADMIN(?, ?, ?, ?)",
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getEmail());
    }

    public AdminEntry getAdmin(int id) throws DataAccessException {
        return template.queryForObject("select * from Admins where ID = ?", rowMapper, id);
    }

    public AdminEntry getAdmin(String username, byte[] passwordHash) throws DataAccessException {
        return template.queryForObject("select * from Admins where username = ? and password = ?",
                rowMapper,
                username,
                passwordHash);
    }
}
