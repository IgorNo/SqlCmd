package ua.com.nov.model.datasource;

import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.repository.DbRepository;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public abstract class AbstractDataSource implements DataSource {

    public AbstractDataSource(Database db) {
        DbRepository.getInstance().addDb(db);
    }

    @Override
    public Connection getConnection() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Connection getConnection(String userName, String password) throws SQLException {
        throw new UnsupportedOperationException();
    }


    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException();
    }
}