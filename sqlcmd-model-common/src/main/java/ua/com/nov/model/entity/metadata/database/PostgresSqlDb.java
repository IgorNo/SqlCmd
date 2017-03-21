package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.constraint.Constraint;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.statement.AbstractColumnSqlStatements;
import ua.com.nov.model.statement.AbstractConstraintSqlStatements;
import ua.com.nov.model.statement.AbstractDbSqlStatements;
import ua.com.nov.model.statement.AbstractTableSqlStatements;

public class PostgresSqlDb extends Database {

    public PostgresSqlDb(String dbUrl, String userName) {
        this(dbUrl, userName, null);
    }

    public PostgresSqlDb(String dbUrl, String userName, String password) {
        this(dbUrl, userName, password, "");
    }

    public PostgresSqlDb(String dbUrl, String userName, String password, String dbProperties) {
        super(dbUrl, userName, password, dbProperties);
        getTypesMap().put(JdbcDataTypes.LONGVARCHAR, "text");
    }

    @Override
    public String getAutoIncrementDefinition() {
        return "";
    }

    @Override
    public String getFullTableName(TableId id) {
        StringBuilder result = new StringBuilder();
        if (id.getSchema() != null) result.append(id.getSchema()).append('.');
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
                return "SELECT datname FROM pg_database WHERE datistemplate = false";
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
                return String.format("ALTER TABLE %s RENAME COLUMN %s TO %s",
                        col.getTableId().getFullName(), col.getName(), col.getNewName());

            }
        };
    }

    @Override
    public AbstractConstraintSqlStatements<PrimaryKey> getPrimaryKeySqlStmtSource() {
        return new ConstraintSqlStatements<>();
    }

    private static class ConstraintSqlStatements<V extends Constraint> extends AbstractConstraintSqlStatements<V> {
        @Override
        public String getUpdateStmt(V value) {
            return String.format("ALTER TABLE %s RENAME CONSTRAINT %s TO %s", value.getTableId().getFullName(),
                    value.getName(), value.getNewName());
        }
    }

    @Override
    public AbstractConstraintSqlStatements<ForeignKey> getForeignKeySqlStmtSource() {
        return new ConstraintSqlStatements<>();
    }
}
