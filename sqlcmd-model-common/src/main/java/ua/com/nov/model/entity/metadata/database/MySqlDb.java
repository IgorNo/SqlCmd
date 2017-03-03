package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.entity.metadata.table.Column;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.statement.AbstractSqlDbStatements;
import ua.com.nov.model.statement.AbstractSqlTableStatements;
import ua.com.nov.model.statement.SqlStatementSource;

public final class MySqlDb extends Database {
    private static final MySqlDbStmts DATABASE_SQL_STATEMENT_SOURCE = new MySqlDbStmts();
    private static final MySqlTableStmts TABLE_SQL_STATEMENT_SOURCE = new MySqlTableStmts();


    public MySqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public MySqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public MySqlDb(String dbUrl, String userName, String password, String dbProperties) {
        super(dbUrl, userName, password, dbProperties);
    }

    @Override
    public String getAutoIncrementDefinition() {
        return " AUTO_INCREMENT";
    }

    @Override
    public MySqlDbStmts getDatabaseSqlStmtSource() {
        return DATABASE_SQL_STATEMENT_SOURCE;
    }

    @Override
    public MySqlTableStmts getTableSqlStmtSource() {
        return TABLE_SQL_STATEMENT_SOURCE;
    }

    @Override
    public SqlStatementSource<TableMdId, Column, TableId> getColumnSqlStmtSource() {
        return null;
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

    private static class MySqlDbStmts extends AbstractSqlDbStatements {
        @Override
        public String getReadAllStmt(Database db) {
            return "SHOW DATABASES";
        }
    }

    private static class MySqlTableStmts extends AbstractSqlTableStatements {

    }
}

