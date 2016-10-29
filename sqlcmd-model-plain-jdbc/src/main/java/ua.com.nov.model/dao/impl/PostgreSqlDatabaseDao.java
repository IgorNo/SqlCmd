package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgreSqlDatabaseDao extends DatabaseDao {
    public static final String SELECT_ALL_AVAILABLE_DB = "SELECT datname FROM pg_database WHERE datistemplate = false";

    @Override
    public Map<String, Database> readAll() throws SQLException {
        Map<String, Database> result = new HashMap<>();
        Statement statement = getDataSource().getConnection().createStatement();
        ResultSet databases = statement.executeQuery(SELECT_ALL_AVAILABLE_DB);

        while (databases.next()) {
            result.put(new Database(databases.getString(1)));
        }
        databases.close();
        statement.close();

        return result;
    }
}
