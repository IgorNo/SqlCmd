package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.AbstractDatabaseMdSqlStatements;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.Database.Id;
import ua.com.nov.model.entity.metadata.datatype.DataType;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseDao extends AbstractDao<Id, Database, Database> {

    @Override
    public Database read(Database.Id eId) throws DaoSystemException {
        Database result = null;
        try {
            Connection conn = getDataSource().getConnection();
            result = createDatabase(eId.getDb(), eId.getName());
            result.addDataTypes(getDataTypes(conn));
            result.addTableTypes(getTableTypes(conn));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Database createDatabase(Database db, String dbName) throws SQLException {
        Class[] paramTypes = new Class[]{String.class, String.class, MetaDataOptions.class};
        Database result = null;
        MetaDataOptions.Builder<?> builder = new OptionsDao<Id, Database>(getDataSource()).read(db.new Id(db.getDbUrl(), dbName));
        Optional<Database> options = null;
        if (builder != null) options = builder.build();
        try {
            Constructor<? extends Database> constructor = db.getClass().getConstructor(paramTypes);
            return constructor.newInstance(new Object[]{db.getDbUrl(), dbName,
                    options});
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

    private List<String> getTableTypes(Connection conn) throws SQLException {
        ResultSet rs = conn.getMetaData().getTableTypes();
        List<String> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rs.getString("TABLE_TYPE"));
        }
        return result;
    }

    @Override
    protected SqlExecutor getExecutor(DataSource dataSource) {
        return new DDLSqlExecutor<>(dataSource);
    }

    @Override
    protected AbstractRowMapper getRowMapper(Database db) {
        return new AbstractRowMapper(db) {
            @Override
            public Database mapRow(ResultSet rs, int i) throws SQLException {
                return createDatabase(db, rs.getString(1));
            }
        };
    }

    @Override
    protected AbstractDatabaseMdSqlStatements<Id, Database, Database> getSqlStmtSource(Database db) {
        return db.getDatabaseSqlStmtSource();
    }
}
