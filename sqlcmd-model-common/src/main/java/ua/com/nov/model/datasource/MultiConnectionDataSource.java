package ua.com.nov.model.datasource;

import ua.com.nov.model.entity.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MultiConnectionDataSource extends AbstractDataSource{
    private Database db;

    public MultiConnectionDataSource(Database db) {
        super(db);
        this.db = db;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(db.getDbUrl(), db.getUserName(), db.getPassword());
    }

    @Override
    public Connection getConnection(String userName, String password) throws SQLException {
        return DriverManager.getConnection(db.getDbUrl(), userName, password);
    }


}
