package ua.com.nov.model.entity.database;

import ua.com.nov.model.dao.BaseSqlStmtSource;
import ua.com.nov.model.datasource.BaseDataSource;
import ua.com.nov.model.entity.Mappable;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.key.ForeignKey;
import ua.com.nov.model.entity.key.Key;
import ua.com.nov.model.entity.row.RowData;
import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.entity.table.TableId;
import ua.com.nov.model.repository.DbRepository;
import ua.com.nov.model.util.DbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public abstract class Database
        extends BaseDataSource
        implements Persistent<Database> {

    private DatabaseId id;
    private String password;
    private List<DataType> dataTypes;
    private String dbProperties = "";

    public Database(DatabaseId id) {
        this(id, null);
    }

    public Database(String dbUrl, String userName) {
        this(new DatabaseId(dbUrl, userName), null);
    }

    public Database(String dbUrl, String userName, String password) {
        this(new DatabaseId(dbUrl, userName), password);
    }

    public Database(DatabaseId id, String password) {
        this.id = id;
        this.password = password;
        DbRepository.addDb(this);
    }

    public abstract AbstractSqlTableStatements getTableSqlStmtSource();

    public abstract String getFullTableName(TableId id);

    @Override
    public Mappable<Database> getRowMapper() {
        throw new UnsupportedOperationException();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getDbUrl(), getUserName(), password);
    }

    // convert 'parameter' to database format (to upper or lower case)
    public abstract String convert(String parameter);

    public void setDataTypes(List<DataType> dataTypes) {
        this.dataTypes = dataTypes;
    }

    public DataType getDataType(String typeName) {
        for (DataType dataType : dataTypes) {
            if (dataType.getTypeName().equalsIgnoreCase(typeName)) return dataType;
        }
        throw new IllegalArgumentException();
    }

    public List<DataType> getListDataType(int jdbcDataType) {
        List<DataType> result = new LinkedList<>();
        for (DataType dataType : dataTypes) {
            if (jdbcDataType == dataType.getJdbcDataType()) result.add(dataType);
        }
        return result;
    }

    public String getDbProperties() {
        return dbProperties;
    }

    public void setDbProperties(String dbProperties) {
        this.dbProperties = dbProperties;
    }

    @Override
    public DatabaseId getId() {
        return id;
    }

    public String getDbUrl() {
        return id.getDbUrl();
    }

    public String getName() {
        return DbUtil.getDatabaseName(getDbUrl());
    }

    public String getUserName() {
        return id.getUserName();
    }

    public String getPassword() {
        return password;
    }

    protected abstract static class AbstractSqlDbStatements extends BaseSqlStmtSource<Database> {

        public static final String CREATE_DB_SQL = "CREATE DATABASE %s %s";
        public static final String DROP_DB_SQL = "DROP DATABASE %s";


        @Override
        public String getCreateStmt(Database db) {
            return String.format(CREATE_DB_SQL, db.getName(), db.getDbProperties());
        }

        @Override
        public String getDeleteStmt(Database db) {
            return String.format(DROP_DB_SQL, db.getName());
        }

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

    protected abstract static class AbstractSqlTableStatements extends BaseSqlStmtSource<Table<? extends RowData>> {
        public static final String CREATE_TABLE_SQL = "CREATE TABLE %s (%s) %s";
        public static final String DROP_TABLE_SQL = "DROP TABLE %s";
        public static final String RENAME_TABLE_SQL = "ALTER TABLE %s RENAME TO %s";

        @Override
        public String getCreateStmt(Table<? extends RowData> table) {
            return String.format(CREATE_TABLE_SQL,
                    table.getFullName(), getCreateTableDefinition(table), table.getTableProperies());
        }

        @Override
        public String getDeleteStmt(Table<? extends RowData> table) {
            return String.format(DROP_TABLE_SQL, table.getName());
        }

        @Override
        public String getUpdateStmt(Table<? extends RowData> table) {
            return String.format(RENAME_TABLE_SQL, table.getId().getName(), table.getName());
        }

        private String getCreateTableDefinition(Table<? extends RowData> table) {
            int numberOfColumns = table.getNumberOfColumns();
            if (numberOfColumns == 0) return "";

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < numberOfColumns; i++) {
                if (i != 0) result.append(", ");
                addColumnDefinition(i + 1, table, result);
            }

            addKey(table.getPrimaryKey(), ", PRIMARY KEY", result);

            for (Key key : table.getUniqueKeyList()) {
                addKey(key, ", UNIQUE", result);
            }

            for (ForeignKey key : table.getForeignKeyList()) {
                addForeignKey(key, result);
            }

            for (String chekExpr : table.getCheckExpressionList()) {
                result.append(", CHECK(").append(chekExpr).append(')');
            }

            return result.toString();
        }

        private void addColumnDefinition(int ordinalPosition, Table table, StringBuilder result) {
            Column col = table.getColumn(ordinalPosition);
            if (col.getName().trim().isEmpty() || col.getDataType().getTypeName().trim().isEmpty()) {
                throw new IllegalArgumentException();
            }

            result.append(col.getName()).append(' ');
            addFullTypeName(col, result);
            addNotNull(col, result);
            addDefaultValue(col, result);
        }

        protected abstract void addFullTypeName(Column col, StringBuilder result);

        protected void addSizeAndPrecision(Column col, StringBuilder result) {
            if (col.getColumnSize() != null) {
                result.append('(').append(col.getColumnSize()).append(')');
                if (col.getPrecision() != null) result.append(',').append(col.getPrecision());
                result.append(')');
            }
        }

        private void addNotNull(Column col, StringBuilder result) {
            if (col.getNullable() == 2) throw new IllegalArgumentException();
            if (col.getNullable() == 1) result.append(" NOT NULL");
        }

        private void addDefaultValue(Column col, StringBuilder result) {
            if (col.getDefaultValue() != null && !col.getDefaultValue().trim().isEmpty()) {
                result.append(" DEFAULT ").append(col.getDefaultValue());
            }
        }

        private void addKey(Key key, String keyType, StringBuilder result) {
            int numberOfKeyColumns = key.getNumberOfColumns();
            if (numberOfKeyColumns > 0) {
                result.append(keyType).append(" (");
                for (int i = 1; i <= numberOfKeyColumns; i++) {
                    result.append(key.getColumn(i));
                    if (i != numberOfKeyColumns) result.append(',');
                }
                result.append(')');
            }
        }

        private void addForeignKey(ForeignKey key, StringBuilder result) {
            int numberOfKeyColumns = key.getNumberOfColumns();
            if (numberOfKeyColumns > 0) {
                result.append(", FOREIGN KEY").append(" (");
                for (int i = 1; i <= numberOfKeyColumns; i++) {
                    result.append(key.getFkColumn(i));
                    if (i != numberOfKeyColumns) result.append(',');
                }
                result.append(')');
                result.append(" REFERENCES ").append(key.getPkColumn(0).getId().getTable().getName()).append(" (");
                for (int i = 1; i <= numberOfKeyColumns; i++) {
                    result.append(key.getPkColumn(i));
                    if (i != numberOfKeyColumns) result.append(',');
                }
                result.append(')');
            }
            if (key.getDeleteRule() != null) {
                result.append(" ON DELETE ").append(key.getDeleteRule().getAction());
            }
            if (key.getUpdateRule() != null) {
                result.append(" ON UPDATE ").append(key.getUpdateRule().getAction());
            }
        }

    }

}
