package ua.com.nov.model.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.SqlStatement;

import javax.sql.DataSource;
import java.util.List;

public class DMLSqlExecutor extends SqlExecutor {
    private JdbcTemplate jdbcTemplate;

    public DMLSqlExecutor() {
    }

    public DMLSqlExecutor(DataSource dataSource) {
        super(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void executeUpdateStmt(SqlStatement sqlStmt) throws DaoSystemException {
        if (sqlStmt != null) {
            try {
                jdbcTemplate.update(sqlStmt.getSql(), sqlStmt.getValues(), sqlStmt.getValueTypes());
            } catch (DataAccessException e) {
                throw new DaoSystemException("DDL DAO update exception.\n", e);
            }
        }
    }

    @Override
    public KeyHolder executeInsertStmt(SqlStatement sqlStmt) throws DaoSystemException {
        try {
            PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlStmt.getSql(),
                    sqlStmt.getSqlParameters());
            pscf.setReturnGeneratedKeys(true);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(pscf.newPreparedStatementCreator(sqlStmt.getValues()), keyHolder);
            return keyHolder;
        } catch (DataAccessException e) {
            throw new DaoSystemException("DDL DAO update exception.\n", e);
        }
    }

    @Override
    public <T> List<T> executeQueryStmt(SqlStatement sqlStmt, RowMapper<T> mapper) throws DaoSystemException {
        return jdbcTemplate.query(sqlStmt.getSql(), sqlStmt.getValues(), sqlStmt.getValueTypes(), mapper);
    }
}
