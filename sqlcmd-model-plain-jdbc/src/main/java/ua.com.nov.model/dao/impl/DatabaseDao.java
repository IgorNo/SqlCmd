package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.SqlStatementSource;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;
import ua.com.nov.model.entity.database.PostgreSqlDb;
import ua.com.nov.model.repository.DbRepository;
import ua.com.nov.model.util.DbUtil;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseDao extends DataDefinitionDao<DatabaseID, Database, Object> {
    protected DatabaseDao() {}

    @Override
    public Map<DatabaseID, Database> readAll(Object nullObject) throws SQLException {
        Map<DatabaseID, Database> result = new HashMap<>();
        Connection conn = getDataSource().getConnection();

        try (Statement stmt = conn.createStatement()) {
            DatabaseMetaData metaData = conn.getMetaData();
            SqlStatementSource source = DbRepository.getDb(new DatabaseID(metaData.getURL(), metaData.getUserName()))
                    .getSqlStmtSource();

            try (ResultSet databases = stmt.executeQuery(source.getReadAllStmt())) {
                while (databases.next()) {
                    String url = DbUtil.getDatabaseUrl(conn) + databases.getString(1);
                    DatabaseID databaseID = new DatabaseID(url, conn.getMetaData().getUserName());
                    result.put(databaseID, new PostgreSqlDb(databaseID));
                }
            }
        }
        return result;
    }

    @Override
    public int count(Object nullObject) throws SQLException {
        return readAll(nullObject).size();
    }
}
