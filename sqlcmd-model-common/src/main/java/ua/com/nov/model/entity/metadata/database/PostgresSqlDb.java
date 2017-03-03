package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.metadata.table.metadata.TableMdId;
import ua.com.nov.model.entity.metadata.table.metadata.Column;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.statement.AbstractSqlDbStatements;
import ua.com.nov.model.statement.AbstractSqlTableStatements;
import ua.com.nov.model.statement.SqlStatementSource;

public class PostgresSqlDb extends Database {
    private static final PostgreSqlDbStmts DATABASE_SQL_STATEMENT_SOURCE = new PostgreSqlDbStmts();
    private static final PostgreSqlTableStmts TABLE_SQL_STATEMENT_SOURCE = new PostgreSqlTableStmts();

    public PostgresSqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public PostgresSqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public PostgresSqlDb(String dbUrl, String userName, String password, String dbProperties) {
        super(dbUrl, userName, password, dbProperties);
    }

    @Override
    public String getAutoIncrementDefinition() {
        return "";
    }

    @Override
    public PostgreSqlDbStmts getDatabaseSqlStmtSource() {
        return DATABASE_SQL_STATEMENT_SOURCE;
    }

    @Override
    public PostgreSqlTableStmts getTableSqlStmtSource() {
        return TABLE_SQL_STATEMENT_SOURCE;
    }

    @Override
    public SqlStatementSource<TableMdId, Column, TableId> getColumnSqlStmtSource() {
        return null;
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

    private static class PostgreSqlDbStmts extends AbstractSqlDbStatements {
        @Override
        public String getReadAllStmt(Database db) {
            return "SELECT datname FROM pg_database WHERE datistemplate = false";
        }
    }

    private static class PostgreSqlTableStmts extends AbstractSqlTableStatements {
    }

}
