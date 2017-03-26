package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.Key;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;
import ua.com.nov.model.statement.*;

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
        return new AbstractConstraintSqlStatements<PrimaryKey>() {
            @Override
            public String getDeleteStmt(TableMdId id) {
                return String.format("ALTER TABLE %s DROP PRIMARY KEY", id.getTableId().getFullName());
            }
        };
    }

    @Override
    public AbstractConstraintSqlStatements<ForeignKey> getForeignKeySqlStmtSource() {
        return new AbstractConstraintSqlStatements<ForeignKey>() {
            @Override
            public String getDeleteStmt(TableMdId id) {
                return String.format("ALTER TABLE %s DROP FOREIGN KEY %s", id.getTableId().getFullName(), id.getName());
            }
        };
    }

    private abstract static class KeySqlStatements<V extends Key> extends AbstractConstraintSqlStatements<V> {
        @Override
        public String getUpdateStmt(Key pk) {
            return String.format("ALTER TABLE %s RENAME INDEX %s TO %s", pk.getTableId().getFullName(),
                    pk.getName(), pk.getNewName());
        }

        @Override
        public String getDeleteStmt(TableMdId id) {
            return String.format("ALTER TABLE %s DROP INDEX %s", id.getTableId().getFullName(), id.getName());
        }

    }

    @Override
    public AbstractConstraintSqlStatements<UniqueKey> getUniqueKeySqlStmtSource() {
        return new KeySqlStatements<UniqueKey>() {};
    }

    @Override
    public AbstractIndexSqlStatements getIndexSqlStmtSource() {
        return new AbstractIndexSqlStatements() {
            @Override
            public String getDeleteStmt(TableMdId id) {
                return super.getDeleteStmt(id) + " ON " + id.getTableId().getFullName();
            }
        };
    }
}

