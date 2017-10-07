package ua.com.nov.model.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.dao.statement.SqlStatement;

import javax.sql.DataSource;
import java.util.List;

public class DDLSqlExecutor extends SqlExecutor {
    private JdbcTemplate jdbcTemplate;

    public DDLSqlExecutor() {
    }

    public DDLSqlExecutor(DataSource dataSource) {
        super(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void executeUpdateStmt(SqlStatement sqlStmt) throws DAOSystemException {
        String[] sqlArray = sqlStmt.getSql().split(";");
        try {
            for (String sql : sqlArray) {
                if (!sql.isEmpty())
                    jdbcTemplate.update(sql);
            }
        } catch (DataAccessException e) {
            throw new DAOSystemException("DDL DAO update exception.\n", e);
        }
    }

    @Override
    public <T> List<T> executeQueryStmt(SqlStatement sqlStmt, RowMapper<T> mapper) throws DAOSystemException {
        try {
            return jdbcTemplate.query(sqlStmt.getSql(), mapper);
        } catch (DataAccessException e) {
            throw new DAOSystemException("DDL DAO query exception.\n", e);
        }
    }
}
