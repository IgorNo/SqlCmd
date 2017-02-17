package ua.com.nov.model.datasource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSource extends BaseDataSource {
    private final Connection conn;

    public SingleConnectionDataSource(DataSource dataSource) throws SQLException {
        this.conn = dataSource.getConnection();
    }

    @Override
    public Connection getConnection() {
        return conn;
    }
}
