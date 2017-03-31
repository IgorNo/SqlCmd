package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.datasource.BaseDataSource;
import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.Optionable;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;
import ua.com.nov.model.statement.AbstractColumnSqlStatements;
import ua.com.nov.model.statement.AbstractConstraintSqlStatements;
import ua.com.nov.model.statement.AbstractMetaDataSqlStatements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public abstract class Database extends BaseDataSource
        implements Unique<Database.Id>, Persistent<Database>, Optionable {
    private final Id id;
    private String userName;
    private final MetaDataOptions options;

    private Map<String,DataType> dataTypes = new HashMap<>();
    private final Map<JdbcDataTypes, String> typesMap = new HashMap<>();

    public Database(String dbUrl, String dbName, MetaDataOptions options) {
        this.id = new Id(dbUrl, dbName);
        this.options = options;
    }

    protected Map<JdbcDataTypes, String> getTypesMap() {
        return typesMap;
    }

    public abstract String getFullTableName(Table.Id id);

    public abstract String getAutoIncrementDefinition();

    public abstract AbstractMetaDataSqlStatements<Database.Id, Database, Database> getDatabaseSqlStmtSource();

    public AbstractMetaDataSqlStatements<Table.Id, Table, Database.Id> getTableSqlStmtSource(){
        return new AbstractMetaDataSqlStatements<Table.Id, Table, Database.Id>() {};
    }

    public abstract AbstractColumnSqlStatements getColumnSqlStmtSource();

    public AbstractConstraintSqlStatements<PrimaryKey.Id, PrimaryKey> getPrimaryKeySqlStmtSource() {
        return new AbstractConstraintSqlStatements<PrimaryKey.Id, PrimaryKey>(){};
    }

    public AbstractConstraintSqlStatements<ForeignKey.Id, ForeignKey> getForeignKeySqlStmtSource() {
        return new AbstractConstraintSqlStatements<ForeignKey.Id, ForeignKey>(){};
    }

    public AbstractConstraintSqlStatements<UniqueKey.Id, UniqueKey> getUniqueKeySqlStmtSource() {
        return new AbstractConstraintSqlStatements<UniqueKey.Id, UniqueKey>(){};
    }

    public AbstractMetaDataSqlStatements<Index.Id, Index, Table.Id> getIndexSqlStmtSource() {
        return new AbstractMetaDataSqlStatements<Index.Id, Index, Table.Id> (){};
    }

    // convert 'parameter' to database format (to upper or lower case)
    public abstract String convert(String parameter);

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public Database getContainerId() {
        return this;
    }

    @Override
    public Database getDb() {
        return this;
    }

    @Override
    public String getMdName() {
        return id.getMdName();
    }

    @Override
    public String getFullName() {
        return id.getFullName();
    }

    @Override
    public Connection getConnection(String userName, String password) throws SQLException {
        this.userName = userName;
        return DriverManager.getConnection(getDbUrl() + getName(), userName, password);
    }

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

    public MetaDataOptions getMdOptions() {
        return options;
    }

    public String getDbUrl() {
        return id.getDbUrl();
    }

    public String getName() {
        return id.getName();
    }

    public String getUserName() {
        return userName;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DATABASE ").append(id.getName());
        if (options != null)
            sb.append(' ').append(options);
        return sb.toString();
    }

    public class Id extends MetaDataId<Database> {
        private final String dbUrl;

        public Id(String dbUrl, String dbName) {
            super(Database.this, dbName);
            if (dbUrl == null || "".equals(dbUrl)) throw new IllegalArgumentException();
            this.dbUrl = dbUrl;
        }

        @Override
        public String getMdName() {
            return "DATABASE";
        }

        @Override
        public Database getDb() {
            return Database.this;
        }

        public String getDbUrl() {
            return dbUrl;
        }

        @Override
        public String getFullName() {
            return getName();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Id that = (Id) o;

            return getFullName().equalsIgnoreCase(that.getFullName());
        }

        @Override
        public int hashCode() {
            return getFullName().toLowerCase().hashCode();
        }
    }

}
