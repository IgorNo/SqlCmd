package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostgreSqlDataBaseDao extends DataBaseDao {
    public static final String SELECT_ALL_AVAILABLE_DB = "SELECT datname FROM pg_database WHERE datistemplate = false";

    @Override
    public List<DataBase> readAll() throws SQLException {
        List<DataBase> result = new ArrayList<>();
        Statement statement = dataSource.getConnection().createStatement();
        ResultSet databases = statement.executeQuery(SELECT_ALL_AVAILABLE_DB);

        while (databases.next()) {
            result.add(new DataBase(databases.getString(1)));
        }
        databases.close();
        statement.close();

        return result;
    }
}
