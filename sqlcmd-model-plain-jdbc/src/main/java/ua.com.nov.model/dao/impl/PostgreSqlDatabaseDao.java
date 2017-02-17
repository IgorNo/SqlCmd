package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;
import ua.com.nov.model.entity.database.PostgreSqlDb;
import ua.com.nov.model.util.DbUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PostgreSqlDatabaseDao extends DatabaseDao {
    public static final String SELECT_ALL_AVAILABLE_DB = "SELECT datname FROM pg_database WHERE datistemplate = false";

    @Override
    public Map<DatabaseID, Database> readAll() throws SQLException {
        Map<DatabaseID, Database> result = new HashMap<>();
        Connection conn = getDataSource().getConnection();
        Statement statement = conn.createStatement();
        ResultSet databases = statement.executeQuery(SELECT_ALL_AVAILABLE_DB);

        while (databases.next()) {
            String url = DbUtil.getDatabaseUrl(conn) + databases.getString(1);
            DatabaseID databaseID = new DatabaseID(url, conn.getMetaData().getUserName());
            result.put(databaseID, new PostgreSqlDb(databaseID));
        }
        databases.close();
        statement.close();

        return result;
    }
}
