package ua.com.nov.model.datasource;

import ua.com.nov.model.entity.database.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSource extends AbstractDataSource {
    private Connection conn;

    public SingleConnectionDataSource(Database db) {
        super(db);
    }


    @Override
    public synchronized Connection getConnection() throws SQLException {
        if (conn == null) {
            conn = getConnection(getDb(), getDb().getUserName(), getDb().getPassword());
        }
        return conn;
    }

}
