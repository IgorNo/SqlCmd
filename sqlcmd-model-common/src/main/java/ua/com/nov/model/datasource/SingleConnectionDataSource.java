package ua.com.nov.model.datasource;

import org.springframework.jdbc.datasource.SmartDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSource extends BaseDataSource implements SmartDataSource {
    private final Connection conn;

    public SingleConnectionDataSource(DataSource dataSource, String userName, String password) throws SQLException {
        this.conn = dataSource.getConnection(userName, password);
    }

    @Override
    public boolean shouldClose(Connection con) {
        return false;
    }

    @Override
    public Connection getConnection() {
        return conn;
    }
}
