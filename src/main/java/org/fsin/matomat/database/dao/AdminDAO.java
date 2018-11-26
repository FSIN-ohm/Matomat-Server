package org.fsin.matomat.database.dao;

import org.fsin.matomat.BasicDAO;
import org.fsin.matomat.database.model.AdminEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

public class AdminDAO implements BasicDAO<AdminEntry> {
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
        //TODO: implement this adminEntry.setCorespondingUser();
        return adminEntry;
    };

    @Override
    public List<AdminEntry> getAll() {
        return template.query("select * from Admins", rowMapper);
    }
}
