package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.dao.statement.AbstractDatabaseMdSqlStatements;
import ua.com.nov.model.dao.statement.AbstractTableMdSqlStatements;
import ua.com.nov.model.dao.statement.OptionsSqlStmtSource;
import ua.com.nov.model.datasource.BaseDataSource;
import ua.com.nov.model.entity.*;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.column.Column;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public abstract class Database extends BaseDataSource
        implements Unique<Database.Id>, Persistent, Hierarchical<Database> {
    private final Id id;
    protected MetaDataOptions<? extends Database> options;
    protected List<String> tableTypes;
    private String userName;
    private Map<String, DataType> dataTypes = new HashMap<>();
    private Map<JdbcDataTypes, String> typesMap = new HashMap<>();

    public Database(String dbUrl, String dbName, MetaDataOptions<? extends Database> options) {
        this.id = new Id(dbUrl, dbName);
        this.options = options;
    }

    protected Map<JdbcDataTypes, String> getTypesMap() {
        return typesMap;
    }

    public abstract ColumnOptions.Builder<? extends ColumnOptions> createColumnOptions();

    public OptionsSqlStmtSource getOptionsSqlStmtSource(String mdName) {
        switch (mdName) {
            case "DATABASE":
                return getDatabaseOptionsSqlStmtSource();

            case "TABLE":
                return getTableOptionsSqlStmtSource();

            case "COLUMN":
                return getColumnOptionsSqlStmSource();

            default:
                throw new IllegalArgumentException();
        }
    }

    protected OptionsSqlStmtSource<Id, ? extends Database> getDatabaseOptionsSqlStmtSource() {
        return new OptionsSqlStmtSource<Id, Database>() {
        };
    }

    protected OptionsSqlStmtSource<Table.Id, Table> getTableOptionsSqlStmtSource() {
        return new OptionsSqlStmtSource<Table.Id, Table>() {
        };
    }

    protected OptionsSqlStmtSource<Column.Id, Column> getColumnOptionsSqlStmSource() {
        return new OptionsSqlStmtSource<Column.Id, Column>() {
        };
    }

    public abstract AbstractDatabaseMdSqlStatements<Id, Database, Database> getDatabaseSqlStmtSource();

    public <I extends MetaDataId<C>, E extends MetaData<I>, C extends Hierarchical>
    AbstractDatabaseMdSqlStatements<I, E, C> getDatabaseMdSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements() {
        };
    }

    public AbstractDatabaseMdSqlStatements<Table.Id, Table, Schema.Id> getTableSqlStmtSource() {
        return new AbstractDatabaseMdSqlStatements<Table.Id, Table, Schema.Id>() {
            @Override
            protected String getCommentStmt(Table table) {
                AbstractTableMdSqlStatements sqlStmtSource = table.getDb().getTableMdSqlStmtSource();
                StringBuilder sb = new StringBuilder(super.getCommentStmt(table));
                String s = "";
                for (Column column : table.getColumns()) {
                   sb.append(s).append(sqlStmtSource.getCommentStmt(column));
                   s = ";";
                }
                return sb.toString();
            }
        };
    }

    public <I extends TableMd.Id, E extends TableMd>
    AbstractTableMdSqlStatements<I, E> getTableMdSqlStmtSource() {
        return new AbstractTableMdSqlStatements<I, E>() {
        };
    }

    public AbstractTableMdSqlStatements<Column.Id, Column> getColumnSqlStmtSource() {
        return new AbstractTableMdSqlStatements<Column.Id, Column>() {
        };
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
    public Optional<? extends Database> getOptions() {
        return options;
    }

    @Override
    public Connection getConnection(String userName, String password) throws SQLException {
        this.userName = userName;
        return DriverManager.getConnection(getDbUrl() + getName(), userName, password);
    }

    @Override
    public String getType() {
        return getMdName();
    }

    @Override
    public String getViewName() {
        return null;
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


    public void addTableTypes(Collection<String> tableTypes) {
        this.tableTypes = new ArrayList<>(tableTypes);
    }

    public String[] getTableTypes() {
        return tableTypes.toArray(new String[0]);
    }

    /**
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
    public String getCreateStmtDefinition(String conflictOption) {
        final StringBuilder sb = new StringBuilder(getMdName()).append(' ');
        if (conflictOption != null) sb.append(conflictOption).append(' ');
        sb.append(id.getName());
        if (options != null)
            sb.append(' ').append(options);
        return sb.toString();

    }

    @Override
    public String toString() {
        return getCreateStmtDefinition(null);
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
