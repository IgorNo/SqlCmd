package ua.com.nov.model.entity.database;

import ua.com.nov.model.dao.BaseSqlStmtSource;
import ua.com.nov.model.dao.SqlStatementSource;
import ua.com.nov.model.datasource.BaseDataSource;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.key.ForeignKey;
import ua.com.nov.model.entity.key.Key;
import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.entity.table.TableID;
import ua.com.nov.model.repository.DbRepository;
import ua.com.nov.model.util.DbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Database
        extends BaseDataSource
        implements Persistent<DatabaseID, Database> {

    private DatabaseID pk;
    private String password;
    private List<DataType> dataTypes;
    private String dbProperties = "";

    public Database(DatabaseID pk) {
        this(pk, null);
    }

    public Database(String dbUrl, String userName) {
        this(new DatabaseID(dbUrl, userName), null);
    }

    public Database(String dbUrl, String userName, String password) {
        this(new DatabaseID(dbUrl, userName), password);
    }

    public Database(DatabaseID pk, String password) {
        this.pk = pk;
        this.password = password;
        DbRepository.addDb(this);
    }

    public abstract SqlStatementSource<TableID, Table> getTableSqlStmtSource();
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getDbUrl(), getUserName(), password);
    }

    public Database load() throws SQLException {
        Connection conn = getConnection();
        dataTypes = getDataTypes(conn);
        conn.close();
        return this;
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

    public DatabaseID getPk() {
        return pk;
    }

    public String getDbUrl() {
        return pk.getDbUrl();
    }

    public String getName() {
        return DbUtil.getDatabaseName(getDbUrl());
    }

    public String getUserName() {
        return pk.getUserName();
    }

    public String getPassword() {
        return password;
    }

    protected abstract static class AbstractSqlDbStatements extends BaseSqlStmtSource<DatabaseID, Database> {

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

    protected abstract static class AbstractSqlTableStatements extends BaseSqlStmtSource<TableID, Table> {
        public static final String CREATE_TABLE_SQL = "CREATE TABLE %s (%s) %s";
        public static final String DROP_TABLE_SQL = "DROP TABLE %s";
        public static final String RENAME_TABLE_SQL = "ALTER TABLE %s RENAME TO %s";

        @Override
        public String getCreateStmt(Table table) {
            return String.format(CREATE_TABLE_SQL, table.getName(), getCreateTableDefinition(table), table.getTableProperies());
        }

        public String getDropStmt(Table table) {
            return DROP_TABLE_SQL;
        }

        public String getUpdateStmt(Table table) {
            return RENAME_TABLE_SQL;
        }

        private String getCreateTableDefinition(Table table) {
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
                result.append(" REFERENCES ").append(key.getPkColumn(0).getPk().getTableID().getName()).append(" (");
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
