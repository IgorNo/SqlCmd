package ua.com.nov.model.datasource;

import ua.com.nov.model.entity.database.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class MultiConnectionDataSource extends AbstractDataSource{

    public MultiConnectionDataSource(Database db) {
        super(db);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(getDb(), getDb().getUserName(), getDb().getPassword());
    }

    @Override
    public Connection getConnection(String userName, String password) throws SQLException {
        return getConnection(getDb(), userName, password);
    }


}
