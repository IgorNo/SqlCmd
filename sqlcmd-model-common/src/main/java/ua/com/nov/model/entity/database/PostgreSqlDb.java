package ua.com.nov.model.entity.database;

import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.table.TableId;
import ua.com.nov.model.statement.AbstractSqlDbStatements;
import ua.com.nov.model.statement.AbstractSqlTableStatements;

import java.sql.Types;

public class PostgreSqlDb extends Database {
    private static final PostgreSqlDbStmts DATABASE_SQL_STATEMENT_SOURCE = new PostgreSqlDbStmts();
    private static final PostgreSqlTableStmts TABLE_SQL_STATEMENT_SOURCE = new PostgreSqlTableStmts();

    public PostgreSqlDb(DatabaseId pk) {
        super(pk);
    }

    public PostgreSqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public PostgreSqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public PostgreSqlDb(DatabaseId pk, String password) {
        super(pk, password);
    }

    @Override
    public PostgreSqlDbStmts getSqlStmtSource() {
        return DATABASE_SQL_STATEMENT_SOURCE;
    }

    @Override
    public PostgreSqlTableStmts getTableSqlStmtSource() {
        return TABLE_SQL_STATEMENT_SOURCE;
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
        @Override
        protected void addFullTypeName(Column col, StringBuilder result) {
            if (col.isAutoIncrement()) {
                switch (col.getDataType().getJdbcDataType()) {
                    case Types.SMALLINT:
                        result.append("SMALLSERIAL");
                        break;
                    case Types.INTEGER:
                        result.append("SERIAL");
                        break;
                    case Types.BIGINT:
                        result.append("BIGSERIAL");
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } else {
                result.append(col.getDataType().getTypeName());
            }
            addSizeAndPrecision(col, result);
        }
    }

}
