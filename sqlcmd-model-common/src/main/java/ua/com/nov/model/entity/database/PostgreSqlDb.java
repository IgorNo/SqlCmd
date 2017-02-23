package ua.com.nov.model.entity.database;

import ua.com.nov.model.dao.SqlStatementSource;
import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.table.Table;

import java.sql.Types;

public class PostgreSqlDb extends Database {

    private static final SqlStatementSource<Database, Database>
            DATABASE_SQL_STATEMENT_SOURCE = new PostgreSqlDbStmts();
    private static final SqlStatementSource<Table, Database>
            TABLE_SQL_STATEMENT_SOURCE = new PostgreSqlTableStmts();

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
    public SqlStatementSource<Database, Database> getSqlStmtSource() {
        return DATABASE_SQL_STATEMENT_SOURCE;
    }

    @Override
    public SqlStatementSource<Table, Database> getTableSqlStmtSource() {
        return TABLE_SQL_STATEMENT_SOURCE;
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
