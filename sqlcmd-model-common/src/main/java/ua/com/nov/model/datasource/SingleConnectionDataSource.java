package ua.com.nov.model.datasource;

import ua.com.nov.model.entity.database.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleConnectionDataSource extends AbstractDataSource {
    private Database db;
    private Connection conn;

    public SingleConnectionDataSource(Database db) {
        super(db);
        this.db = db;
    }


    @Override
    public synchronized Connection getConnection() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection(db.getDbUrl(), db.getUserName(), db.getPassword());
        }
        return conn;
    }

}
