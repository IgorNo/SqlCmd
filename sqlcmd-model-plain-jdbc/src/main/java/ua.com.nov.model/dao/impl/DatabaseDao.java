package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.database.DataType;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseId;
import ua.com.nov.model.statement.SqlStatementSource;
import ua.com.nov.model.util.DbUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseDao extends DataDefinitionDao<Database> {
    @Override
    public void create(Database db) throws SQLException {
        super.create(db);
    }

    @Override
    public Database readOne(Database db) throws SQLException {
        Connection conn = getDataSource().getConnection();
        db.setDataTypes(getDataTypes(conn));
        return db;
    }

    private List<DataType> getDataTypes(Connection conn) throws SQLException {
        ResultSet rs = conn.getMetaData().getTypeInfo();
        List<DataType> dataTypeList = new ArrayList<>();
        while (rs.next()) {
            DataType dataType = new DataType.Builder(rs.getString("TYPE_NAME"), rs.getInt("DATA_TYPE"))
                    .precision(rs.getInt("PRECISION"))
                    .literalPrefix(rs.getString("LITERAL_PREFIX"))
                    .literalSuffix(rs.getString("LITERAL_SUFFIX"))
                    .createParams(rs.getString("CREATE_PARAMS"))
                    .nullable(rs.getShort("NULLABLE"))
                    .caseSensitive(rs.getBoolean("CASE_SENSITIVE"))
                    .searchable(rs.getShort("SEARCHABLE"))
                    .unsignedAttribute(rs.getBoolean("UNSIGNED_ATTRIBUTE"))
                    .fixedPrecScale(rs.getBoolean("FIXED_PREC_SCALE"))
                    .autoIncrement(rs.getBoolean("AUTO_INCREMENT"))
                    .localTypeName(rs.getString("LOCAL_TYPE_NAME"))
                    .minimumScale(rs.getInt("MINIMUM_SCALE"))
                    .maximumScale(rs.getInt("MAXIMUM_SCALE"))
                    .numPrecRadix(rs.getInt("NUM_PREC_RADIX"))
                    .build();
            dataTypeList.add(dataType);
        }
        return dataTypeList;
    }

    @Override
    public List<Database> readN(int nStart, int number, Database db) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Database> readAll(Database db) throws SQLException {
        List<Database> result = new ArrayList<>();
        Connection conn = getDataSource().getConnection();

        try (Statement stmt = conn.createStatement()) {
            SqlStatementSource source = db.getSqlStmtSource();

            try (ResultSet rs = stmt.executeQuery(source.getReadAllStmt(db))) {
                while (rs.next()) {
                    String url = DbUtil.getDatabaseUrl(conn) + rs.getString(1);
                    String userName = conn.getMetaData().getUserName();
                    if (userName.indexOf('@') > 0) userName = userName.substring(0, userName.indexOf('@'));
                    DatabaseId databaseId = new DatabaseId(url, userName);
                    Class[] paramTypes = new Class[]{DatabaseId.class};
                    Constructor<? extends Database> constructor = db.getClass().getConstructor(paramTypes);
                    Database database = constructor.newInstance(new Object[]{databaseId});
                    result.add(database);
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                throw new IllegalArgumentException();
            }
        }
        return result;
    }

    @Override
    public void update(Database value) throws SQLException {
        super.update(value);
    }

    @Override
    public void delete(Database value) throws SQLException {
        super.delete(value);
    }

    @Override
    public void deleteAll(Database container) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(Database db) throws SQLException {
        return readAll(db).size();
    }

    @Override
    protected ResultSet getOneResultSet(Database db) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getAllResultSet(Database db) throws SQLException {
        Connection conn = getDataSource().getConnection();
        try (Statement stmt = conn.createStatement()) {
            SqlStatementSource source = db.getSqlStmtSource();
            try (ResultSet databases = stmt.executeQuery(source.getReadAllStmt(db))) {
                return databases;
            }
        }
    }

    @Override
    protected ResultSet getNResultSet(int nStart, int number, Database template) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
