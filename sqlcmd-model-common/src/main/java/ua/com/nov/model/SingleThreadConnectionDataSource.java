package ua.com.nov.model;

import ua.com.nov.model.entity.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleThreadConnectionDataSource extends AbstractDataSource {
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    private Database db;
    private String url;

    public SingleThreadConnectionDataSource(String url, Database db) throws SQLException{
        this.db = db;
        this.url = url;
        connectionHolder.set(DriverManager.getConnection(url + db.getDbName(), db.getUserName(), db.getPassword()));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionHolder.get();
    }

}
