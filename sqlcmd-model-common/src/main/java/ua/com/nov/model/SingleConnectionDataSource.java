package ua.com.nov.model;

import ua.com.nov.model.entity.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleConnectionDataSource extends AbstractDataSource {
    private Connection conn;

    public SingleConnectionDataSource(String url, Database db) throws SQLException{
        conn = DriverManager.getConnection(url + db.getDbName(), db.getUserName(), db.getPassword());
    }

    @Override
    public Connection getConnection() throws SQLException {
        return conn;
    }
}
