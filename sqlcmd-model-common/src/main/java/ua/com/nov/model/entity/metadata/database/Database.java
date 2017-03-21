package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.datasource.BaseDataSource;
import ua.com.nov.model.entity.Child;
import ua.com.nov.model.entity.Mappable;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.AbstractMetaDataId;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.statement.AbstractColumnSqlStatements;
import ua.com.nov.model.statement.AbstractConstraintSqlStatements;
import ua.com.nov.model.statement.AbstractDbSqlStatements;
import ua.com.nov.model.statement.AbstractTableSqlStatements;
import ua.com.nov.model.util.DbUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class Database extends BaseDataSource implements Unique<Database.DbId>, Child<Database>, Persistent {
    private final DbId id;
    private final String password;
    private Map<String,DataType> dataTypes = new HashMap<>();
    private final String dbProperties;

    private final Map<JdbcDataTypes, String> typesMap = new HashMap<>();

    public Database(String dbUrl, String userName, String password, String dbProperties) {
        this.id = new DbId(dbUrl, userName);
        this.password = password;
        this.dbProperties = dbProperties;
    }

    protected Map<JdbcDataTypes, String> getTypesMap() {
        return typesMap;
    }

    public abstract AbstractDbSqlStatements getDatabaseSqlStmtSource();

    public abstract AbstractTableSqlStatements getTableSqlStmtSource();

    public abstract AbstractColumnSqlStatements getColumnSqlStmtSource();

    public abstract AbstractConstraintSqlStatements<PrimaryKey> getPrimaryKeySqlStmtSource();

    public abstract AbstractConstraintSqlStatements<ForeignKey> getForeignKeySqlStmtSource();

    public abstract String getFullTableName(TableId id);

    public abstract String getAutoIncrementDefinition();

    @Override
    public Database getContainerId() {
        return this;
    }

    @Override
    public Database getDb() {
        return this;
    }

    @Override
    public String getFullName() {
        return getDbUrl();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getDbUrl(), getUserName(), password);
    }

    // convert 'parameter' to database format (to upper or lower case)
    public abstract String convert(String parameter);

    public void addDataTypes(Collection<DataType> dataTypeList) {
        for (DataType dataType : dataTypeList) {
            dataTypes.put(dataType.getTypeName(), dataType);
        }
    }

    public DataType getDataType(String typeName) {
        DataType dataType = dataTypes.get(typeName);
        if (dataType == null)
            throw new IllegalArgumentException(String.format("Data type '%s' dosn't exist in this database", typeName));
        return dataType;
    }

    /**
     *
     * @param jdbcDataType
     * @return List of DataType elements of that have jdbcDataType
     */
    public List<DataType> getDataTypes(int jdbcDataType) {
        List<DataType> result = new LinkedList<>();
        for (DataType dataType : dataTypes.values()) {
            if (jdbcDataType == dataType.getJdbcDataType()) result.add(dataType);
        }
        return result;
    }

    /**
     *
     * @param jdbcDataType
     * @return List of DataType elements of that have jdbcDataType and are autoincremental
     */
    public List<DataType> getAutoincrementalDataTypes(int jdbcDataType) {
        List<DataType> result = new LinkedList<>();
        for (DataType dataType : dataTypes.values()) {
            if (dataType.isAutoIncrement() && jdbcDataType == dataType.getJdbcDataType()) result.add(dataType);
        }
        return result;
    }

    public DataType getMostApproximateDataTypes(JdbcDataTypes type) {
        String dataTypeName = getTypesMap().get(type);
        if (dataTypeName != null)
            return getDataType(dataTypeName);
        else {
            List<DataType> dataTypes = getDataTypes(type.getJdbcDataType());
            if (dataTypes.size() == 0) {
                throw new IllegalArgumentException(String.format("Data type %s does not support this database", type));
            }
            for (DataType dataType : dataTypes) {
                if (dataType.getTypeName().equalsIgnoreCase(type.toString())) return dataType;
            }
            return dataTypes.get(0);
        }
    }

    public String getDbProperties() {
        return dbProperties;
    }

    @Override
    public DbId getId() {
        return id;
    }

    public String getDbUrl() {
        return id.getDbUrl();
    }

    public String getName() {
        return id.getName();
    }

    public String getUserName() {
        return id.getUserName();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Database database = (Database) o;

        return id.equals(database.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public class DbId extends AbstractMetaDataId<Database> implements Persistent{
        private final String dbUrl;
        private final String userName;
        private final DbRowMapper rowMapper = new DbRowMapper();

        public DbId(String dbUrl, String userName) {
            super(Database.this, userName);
            if (dbUrl == null || "".equals(dbUrl)) throw new IllegalArgumentException();
            this.dbUrl = dbUrl;
            this.userName = userName;
        }

        @Override
        public Database getDb() {
            return Database.this;
        }

        public String getDbUrl() {
            return dbUrl;
        }

        public Database getDatabase() {
            return Database.this;
        }

        public String getName() {
            return DbUtil.getDatabaseName(getDbUrl());
        }

        @Override
        public String getFullName() {
            return Database.this.getFullName();
        }

        public String getUserName() {
            return userName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DbId that = (DbId) o;

            if (!dbUrl.equalsIgnoreCase(that.dbUrl)) return false;
            return userName.equals(that.userName);
        }

        @Override
        public int hashCode() {
            int result = dbUrl.toLowerCase().hashCode();
            result = 31 * result + userName.toLowerCase().hashCode();
            return result;
        }

        public class DbRowMapper implements Mappable {
            @Override
            public Database rowMap(ResultSet rs) throws SQLException {
                String url = DbUtil.getDatabaseUrl(dbUrl) + rs.getString(1);
                Class[] paramTypes = new Class[]{String.class, String.class};
                Database db = null;
                try {
                    Constructor<? extends Database> constructor = Database.this.getClass().getConstructor(paramTypes);
                    db = constructor.newInstance(new Object[]{url, userName});
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return db;
            }
        }
    }

}
