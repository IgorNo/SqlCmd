package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.metadata.datatype.JdbcDataTypes;
import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.statement.AbstractDbSqlStatements;
import ua.com.nov.model.statement.AbstractTableSqlStatements;
import ua.com.nov.model.statement.AbstractlColumnSqlStatements;

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
        return new DbSqlStmts();
    }

    private static class DbSqlStmts extends AbstractDbSqlStatements {
        @Override
        public String getReadAllStmt(Database db) {
            return "SHOW DATABASES";
        }
    }

    @Override
    public AbstractTableSqlStatements getTableSqlStmtSource() {
        return new TableSqlStmts();
    }

    private static class TableSqlStmts extends AbstractTableSqlStatements {
    }


    @Override
    public AbstractlColumnSqlStatements getColumnSqlStmtSource() {
        return new ColumnSqlStatements();
    }

    private static class ColumnSqlStatements extends AbstractlColumnSqlStatements {
        @Override
        public String getUpdateStmt(Column col) {
            return String.format("ALTER TABLE %s CHANGE COLUMN %s %s %s",
                    col.getTableId().getFullName(), col.getName(), col.getNewName(), col.getFullTypeDeclaration());
        }
    }
}

