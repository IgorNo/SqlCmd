package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabasePK;
import ua.com.nov.model.entity.database.PostgreSqlDb;
import ua.com.nov.model.util.DataSourceUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PostgreSqlDatabaseDao extends DatabaseDao {
    public static final String SELECT_ALL_AVAILABLE_DB = "SELECT datname FROM pg_database WHERE datistemplate = false";

    @Override
    public Map<DatabasePK, Database> readAll() throws SQLException {
        Map<DatabasePK, Database> result = new HashMap<>();
        Connection conn = getDataSource().getConnection();
        Statement statement = conn.createStatement();
        ResultSet databases = statement.executeQuery(SELECT_ALL_AVAILABLE_DB);

        while (databases.next()) {
            String url = DataSourceUtil.getDatabaseUrl(conn) + databases.getString(1);
            DatabasePK databasePK = new DatabasePK(url, conn.getMetaData().getUserName());
            result.put(databasePK, new PostgreSqlDb(databasePK));
        }
        databases.close();
        statement.close();

        return result;
    }
}
