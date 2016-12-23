package ua.com.nov.model.datasource;

import ua.com.nov.model.entity.database.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleThreadConnectionDataSource extends AbstractDataSource {
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public SingleThreadConnectionDataSource(Database db) throws SQLException{
        super(db);
        connectionHolder.set(DriverManager.getConnection(db.getDbUrl(), db.getUserName(), db.getPassword()));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionHolder.get();
    }
}