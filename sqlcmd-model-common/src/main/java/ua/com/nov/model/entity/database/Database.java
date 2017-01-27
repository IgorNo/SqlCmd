package ua.com.nov.model.entity.database;

import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.key.ForeignKey;
import ua.com.nov.model.entity.key.Key;
import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.util.DataSourceUtil;

public abstract class Database {
    private DatabaseID pk;
    private String password;
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
    }

    public String getDbProperties() {
        return dbProperties;
    }

    public void setDbProperties(String dbProperties) {
        this.dbProperties = dbProperties;
    }

    public abstract Executable getExecutor();

    public DatabaseID getPk() {
        return pk;
    }

    public String getDbUrl() {
        return pk.getDbUrl();
    }

    public String getName() {
        return DataSourceUtil.getDatabaseName(getDbUrl());
    }

    public String getUserName() {
        return pk.getUserName();
    }

    public String getPassword() {
        return password;
    }

    protected abstract class Executor implements Executable {

        public static final String CREATE_DB_SQL = "CREATE DATABASE %s %s";
        public static final String DROP_DB_SQL = "DROP DATABASE %s";

        public static final String CREATE_TABLE_SQL = "CREATE TABLE %s (%s) %s";
        public static final String DROP_TABLE_SQL = "DROP TABLE %s";
        public static final String RENAME_TABLE_SQL = "ALTER TABLE %s RENAME TO %s";


        @Override
        public String getCreateDbStmt() {
            return String.format(CREATE_DB_SQL, getName(), getDbProperties());
        }

        @Override
        public String getDropDbStmt() {
            return String.format(DROP_DB_SQL, getName());
        }

        @Override
        public String getAlterDbStmt() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getSelectAllDbStmt() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getCreateTableStmt(Table table) {
            return String.format(CREATE_TABLE_SQL, table.getName(), getCreateTableDefinition(table), table.getTableProperies());
        }

        @Override
        public String getDropTableStmt(Table table) {
            return DROP_TABLE_SQL;
        }

        @Override
        public String getUpdateTableStmt(Table table) {
            return RENAME_TABLE_SQL;
        }

        private String getCreateTableDefinition(Table table) {
            int numberOfColumns = table.getNumberOfColumns();
            if (numberOfColumns == 0) return "";

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < numberOfColumns; i++) {
                if (i != 0) result.append(',');
                addColumnDefinition(i, table, result);

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

            result.append(')');
            return result.toString();
        }

        private void addColumnDefinition(int i, Table table, StringBuilder result) {
            Column col = table.getColumn(i);
            if (col.getName().equals("") || col.getTypeName().trim().isEmpty()) throw new IllegalArgumentException();

            addFullTypeName(col, result);
            addNotNull(col, result);
            addDefaultValue(col, result);
        }

        protected void addFullTypeName(Column col, StringBuilder result) {
            result.append(col.getName()).append(' ').append(col.getTypeName());
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
        }

    }
}
