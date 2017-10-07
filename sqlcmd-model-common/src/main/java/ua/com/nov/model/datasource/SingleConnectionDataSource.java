package ua.com.nov.model.datasource;

import org.springframework.jdbc.datasource.SmartDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleConnectionDataSource extends BaseDataSource implements SmartDataSource {
    private Connection conn;
    private String url;
    private String userName;
    private String password;


    public SingleConnectionDataSource(String url, String dbName, String userName, String password) {
        this.url = url + dbName;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public boolean shouldClose(Connection con) {
        return false;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(this.url, userName, password);
        }
        return conn;
    }

}
