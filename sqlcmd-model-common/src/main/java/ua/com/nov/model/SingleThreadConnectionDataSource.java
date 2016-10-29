package ua.com.nov.model;

import ua.com.nov.model.entity.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleThreadConnectionDataSource extends AbstractDataSource {
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public SingleThreadConnectionDataSource(Database db) throws SQLException{
        connectionHolder.set(DriverManager.getConnection(db.getDbUrl(), db.getUserName(), db.getPassword()));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionHolder.get();
    }
}
