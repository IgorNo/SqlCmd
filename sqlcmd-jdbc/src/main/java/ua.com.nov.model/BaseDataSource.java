package ua.com.nov.model;

import ua.com.nov.model.entity.DataBase;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public abstract class BaseDataSource implements DataSource {

    DataBase db;

    protected abstract String getUrl();

    public BaseDataSource(DataBase db) {
        this.db = db;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl() + db.getDbName() , db.getUserName(), db.getPassword());
    }

    @Override
    public Connection getConnection(String userName, String password) throws SQLException {
        return DriverManager.getConnection(getUrl() + db.getDbName(), userName, password);
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
