package ua.com.nov.model.dao.impl;

import ua.com.nov.model.entity.database.DataType;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.Database.DatabaseId;
import ua.com.nov.model.statement.SqlStatementSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseDao extends DataDefinitionDao<DatabaseId, Database> {

    @Override
    public Database read(DatabaseId id) throws SQLException {
        Connection conn = getDataSource().getConnection();
        id.getDatabase().setDataTypes(getDataTypes(conn));
        return id.getDatabase();
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
    public List<Database> readN(int nStart, int number, DatabaseId id) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(DatabaseId key) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(DatabaseId id) throws SQLException {
        return readAll(id).size();
    }

    @Override
    protected ResultSet getOneResultSet(DatabaseId key) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ResultSet getAllResultSet(DatabaseId id) throws SQLException {
        Connection conn = getDataSource().getConnection();
        SqlStatementSource source = id.getSqlStmtSource();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(source.getReadAllStmt(id));
    }

    @Override
    protected ResultSet getNResultSet(int nStart, int number, DatabaseId template) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
