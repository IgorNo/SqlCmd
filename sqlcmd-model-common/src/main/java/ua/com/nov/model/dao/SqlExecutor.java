package ua.com.nov.model.dao;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.SqlStatement;

import javax.sql.DataSource;
import java.util.List;

public abstract class SqlExecutor {
    private DataSource dataSource;

    public SqlExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public SqlExecutor setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public abstract void executeUpdateStmt(SqlStatement sqlStmt) throws DaoSystemException;

    public abstract <T> List<T> executeQueryStmt(SqlStatement sqlStmt, RowMapper<T> mapper) throws DaoSystemException;

    public <T> T executeQueryForObjectStmt(SqlStatement sqlStmt, RowMapper<T> rowMapper) throws DaoSystemException {
        return DataAccessUtils.requiredSingleResult(executeQueryStmt(sqlStmt, rowMapper));
    }

    public <T> T executeQueryForObjectStmt(SqlStatement sqlStmt, Class<T> requiredType) throws DaoSystemException {
        return executeQueryForObjectStmt(sqlStmt, new SingleColumnRowMapper<T>(requiredType));
    }

}
