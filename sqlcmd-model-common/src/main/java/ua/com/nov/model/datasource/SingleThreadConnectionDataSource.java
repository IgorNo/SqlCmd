package ua.com.nov.model.datasource;

import org.springframework.jdbc.datasource.SmartDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleThreadConnectionDataSource extends BaseDataSource implements SmartDataSource {
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public SingleThreadConnectionDataSource(DataSource dataSource, String userName, String password) throws SQLException {
        connectionHolder.set(dataSource.getConnection(userName, password));
    }

    @Override
    public boolean shouldClose(Connection con) {
        return false;
    }

    @Override
    public Connection getConnection() {
        return connectionHolder.get();
    }
}
