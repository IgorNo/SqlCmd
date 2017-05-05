package ua.com.nov.model.dao;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.SqlStatement;

import javax.sql.DataSource;
import java.util.List;

public abstract class SqlExecutor<E> {
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

    public abstract List<E> executeQueryStmt(SqlStatement sqlStmt, RowMapper mapper) throws DaoSystemException;

}
