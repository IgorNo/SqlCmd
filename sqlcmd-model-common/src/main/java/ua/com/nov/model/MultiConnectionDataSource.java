package ua.com.nov.model;

import ua.com.nov.model.entity.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MultiConnectionDataSource extends AbstractDataSource{
    private Database db;
    private String url;

    public MultiConnectionDataSource(String url, Database db) {
        this.db = db;
        this.url = url;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url + db.getDbName(), db.getUserName(), db.getPassword());
    }

    @Override
    public Connection getConnection(String userName, String password) throws SQLException {
        return DriverManager.getConnection(url + db.getDbName(), userName, password);
    }


}
