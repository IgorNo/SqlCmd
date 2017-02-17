package ua.com.nov.model.datasource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleThreadConnectionDataSource extends BaseDataSource {
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public SingleThreadConnectionDataSource(DataSource dataSource) throws SQLException {
        connectionHolder.set(dataSource.getConnection());
    }

    @Override
    public Connection getConnection() {
        return connectionHolder.get();
    }
}
