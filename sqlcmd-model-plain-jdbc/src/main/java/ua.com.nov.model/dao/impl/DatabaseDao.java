package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.SqlStatementSource;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;
import ua.com.nov.model.repository.DbRepository;
import ua.com.nov.model.util.DbUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseDao extends DataDefinitionDao<DatabaseID, Database, Database> {

    @Override
    public Map<DatabaseID, Database> readAll(Database db) throws SQLException {
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
                    Class[] paramTypes = new Class[]{DatabaseID.class};
                    Constructor<? extends Database> constructor = db.getClass().getConstructor(paramTypes);
                    Database database = constructor.newInstance(new Object[]{databaseID});
                    result.put(databaseID, database);
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                throw new IllegalArgumentException();
            }
        }
        return result;
    }

    @Override
    public int count(Database nullObject) throws SQLException {
        return readAll(nullObject).size();
    }
}
