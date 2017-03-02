package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.metadata.database.DataType;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.Database.DbId;
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

public final class DatabaseDao extends DataDefinitionDao<DbId, Database, Database> {

    @Override
    public Database read(DbId dbId) throws SQLException {
        Connection conn = getDataSource().getConnection();
        dbId.getDatabase().setDataTypes(getDataTypes(conn));
        return dbId.getDatabase();
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
    public void deleteAll(Database db) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getResultSet(DbId key) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getResultSet(int nStart, int number, Database db) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getResultSet(Database db) throws SQLException {
        Connection conn = getDataSource().getConnection();
        SqlStatementSource source = getSqlStmtSource(db);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(source.getReadAllStmt(db));
    }

    @Override
    protected Database rowMap(Database db, ResultSet rs) throws SQLException {
        String url = DbUtil.getDatabaseUrl(db.getDbUrl()) + rs.getString(1);
        Class[] paramTypes = new Class[]{String.class, String.class};
        Database result = null;
        try {
            Constructor<? extends Database> constructor = db.getClass().getConstructor(paramTypes);
            return constructor.newInstance(new Object[]{url, db.getUserName()});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected SqlStatementSource<DbId, Database, Database> getSqlStmtSource(Database db) {
        return db.getDatabaseSqlStmtSource();
    }
}
