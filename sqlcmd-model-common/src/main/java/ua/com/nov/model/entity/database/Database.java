package ua.com.nov.model.entity.database;

import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.util.DataSourceUtil;

public abstract class Database {
    private DatabaseID pk;
    private String password;

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

    public abstract String getProperties();

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

        public static final String CREATE_TABLE_SQL = "CREATE TABLE %s (%s)";
        public static final String DROP_TABLE_SQL = "DROP TABLE %s";
        public static final String RENAME_TABLE_SQL = "ALTER TABLE %s RENAME TO %s";


        @Override
        public String getCreateDbStmt() {
            return String.format(CREATE_DB_SQL, getName(), getProperties());
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
            return String.format(CREATE_TABLE_SQL, table.getName(), getCreateTableDefinition(table));
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
                Column col = table.getColumn(i);
                if (col.getName().equals("") || col.getTypeName().equals("")) throw new IllegalArgumentException();

                result.append(col.getName()).append(' ').append(col.getTypeName());

                if (col.getColumnSize() != null) {
                    result.append('(').append(col.getColumnSize()).append(')');
                    if (col.getPrecision() != null) result.append(',').append(col.getPrecision());
                    result.append(')');
                }
                if (col.getNullable() == 2) throw new IllegalArgumentException();
                if (col.getNullable() == 1) result.append(" NOT NULL");
                if (col.getDefaultValue() != null) result.append(" DEFAULT ").append(col.getDefaultValue());
                if (i != numberOfColumns - 1) result.append(',');
            }

            int numberOfPrimaryKeyColumns = table.getNumberOfPrimaryKeyColumns();
            if (numberOfPrimaryKeyColumns > 0) {
                result.append(", ").append("PRIMARY KEY (");
                for (int i = 0; i < numberOfPrimaryKeyColumns; i++) {
                    result.append(table.getPrimaryKeyColumnName(i));
                    if (i != numberOfPrimaryKeyColumns - 1) result.append(',');
                }
                result.append(')');
            }

            return result.append(')').toString();
        }
    }
}
