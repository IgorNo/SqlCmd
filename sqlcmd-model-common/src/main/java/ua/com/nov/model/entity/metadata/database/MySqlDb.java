package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.entity.metadata.table.constraint.Key;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.statement.AbstractColumnSqlStatements;
import ua.com.nov.model.statement.AbstractConstraintSqlStatements;
import ua.com.nov.model.statement.AbstractDbSqlStatements;
import ua.com.nov.model.statement.AbstractTableSqlStatements;

public final class MySqlDb extends Database {

    public MySqlDb(String dbUrl, String userName) {
        this(dbUrl, userName, null);
    }

    public MySqlDb(String dbUrl, String userName, String password) {
        this(dbUrl, userName, password, "");
    }

    public MySqlDb(String dbUrl, String userName, String password, String dbProperties) {
        super(dbUrl, userName, password, dbProperties);
        getTypesMap().put(JdbcDataTypes.INTEGER, "INT");
        getTypesMap().put(JdbcDataTypes.LONGVARCHAR, "TEXT");
        getTypesMap().put(JdbcDataTypes.NUMERIC, "DECIMAL");
    }

    @Override
    public String getAutoIncrementDefinition() {
        return " AUTO_INCREMENT";
    }

    @Override
    public String getFullTableName(TableId id) {
        StringBuilder result = new StringBuilder();
        if (id.getCatalog() != null) result.append(id.getCatalog()).append('.');
        return result.append(id.getName()).toString();
    }

    @Override
    public String convert(String parameter) {
        if (parameter != null) return parameter.toLowerCase();
        return parameter;
    }

    @Override
    public AbstractDbSqlStatements getDatabaseSqlStmtSource() {
        return new AbstractDbSqlStatements() {
            @Override
            public String getReadAllStmt(Database db) {
                return "SHOW DATABASES";
            }
        };
    }

    @Override
    public AbstractTableSqlStatements getTableSqlStmtSource() {
        return new AbstractTableSqlStatements() { };
    }

    @Override
    public AbstractColumnSqlStatements getColumnSqlStmtSource() {
        return new AbstractColumnSqlStatements() {
            @Override
            public String getUpdateStmt(Column col) {
                return String.format("ALTER TABLE %s CHANGE COLUMN %s %s %s",
                        col.getTableId().getFullName(), col.getName(), col.getNewName(), col.getFullTypeDeclaration());
            }
        };
    }

    @Override
    public AbstractConstraintSqlStatements<PrimaryKey> getPrimaryKeySqlStmtSource() {
        return new KeySqlStatements<PrimaryKey>() {
            @Override
            public String getDeleteStmt(TableMdId id) {
                return String.format("ALTER TABLE %s DROP PRIMARY KEY", id.getTableId().getFullName());
            }

            @Override
            public String getUpdateStmt(Key pk) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private abstract static class KeySqlStatements<V extends Key> extends AbstractConstraintSqlStatements<V> {
        @Override
        public String getUpdateStmt(Key pk) {
            return String.format("ALTER TABLE %s RENAME KEY %s TO %s", pk.getTableId().getFullName(),
                    pk.getName(), pk.getNewName());
        }
    }
}

