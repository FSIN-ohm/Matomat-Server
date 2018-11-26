package org.fsin.matomat.database.dao;

import org.fsin.matomat.BasicDAO;
import org.fsin.matomat.database.model.UserEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;

public class UsersDAO implements BasicDAO<UserEntry> {
    JdbcTemplate template;
    public UsersDAO(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    private RowMapper<UserEntry> rowMapper = (ResultSet rs, int rowNum) -> {
        UserEntry entry = new UserEntry();
        entry.setId(rs.getInt("ID"));
        entry.setAuthHash(rs.getBytes("auth_hash"));
        entry.setBalance(rs.getInt("Balance"));
        entry.setLastSeen(rs.getDate("LastSeen"));
        return entry;
    };

    @Override
    public List<UserEntry> getAll() {
        return template.query("select * from Users", rowMapper);
    }
}
