package ua.com.nov.model.dao;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.statement.SqlStatement;

import javax.sql.DataSource;
import java.util.List;

public abstract class SqlExecutor {
    private DataSource dataSource;

    public SqlExecutor() {
    }

    public SqlExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public abstract void executeUpdateStmt(SqlStatement sqlStmt) throws MappingSystemException;

    public abstract <T> List<T> executeQueryStmt(SqlStatement sqlStmt, RowMapper<T> mapper) throws MappingSystemException;

    public KeyHolder executeInsertStmt(SqlStatement sqlStmt) throws MappingSystemException {
        return null;
    }

    public <T> T executeQueryForObjectStmt(SqlStatement sqlStmt, RowMapper<T> rowMapper) throws MappingSystemException {
        return DataAccessUtils.requiredSingleResult(executeQueryStmt(sqlStmt, rowMapper));
    }

    public <T> T executeQueryForObjectStmt(SqlStatement sqlStmt, Class<T> requiredType) throws MappingSystemException {
        return executeQueryForObjectStmt(sqlStmt, new SingleColumnRowMapper<T>(requiredType));
    }

}
