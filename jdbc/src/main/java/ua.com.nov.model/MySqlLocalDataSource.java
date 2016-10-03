package ua.com.nov.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlLocalDataSource extends BaseDataSource {

    public static String DB_URL = "jdbc:mysql://localhost:3306/";

    public MySqlLocalDataSource(String dbName, String userName, String password) {
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL + dbName, userName, password);
    }

    @Override
    public Connection getConnection(String userName, String password) throws SQLException {
        return DriverManager.getConnection(DB_URL + dbName, userName, password);
    }

}
