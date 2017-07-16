package ua.com.nov.model.dao.tx;

import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.BaseDataSource;
import ua.com.nov.model.util.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManagerImpl extends BaseDataSource implements TransactionManager {

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();
    private DataSource dataSource;

    public TransactionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> T doInTransaction(UnitOfWork<T> unitOfWork) throws DaoSystemException {
        Connection conn = null;
        try {
            dataSource.getConnection();
            conn.setAutoCommit(false);
            connectionHolder.set(conn);
            T result = unitOfWork.doInTx();
            conn.commit();
            return result;
        } catch (SQLException | DaoSystemException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new DaoSystemException("Transaction Manager exception\n", e);
        } finally {
            JdbcUtils.closeQuietly(conn);
            connectionHolder.remove();
        }
    }

    @Override
    public Connection getConnection() {
        return connectionHolder.get();
    }

}

