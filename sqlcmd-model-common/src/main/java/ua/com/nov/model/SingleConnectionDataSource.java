package ua.com.nov.model;

import ua.com.nov.model.entity.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleConnectionDataSource extends AbstractDataSource {
    private Database db;
    private String url;
    private Connection conn;

    public SingleConnectionDataSource(String url, Database db) {
        this.db = db;
        this.url = url;
    }


    @Override
    public synchronized Connection getConnection() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection(url + db.getDbName(), db.getUserName(), db.getPassword());
        }
        return conn;
    }

}
