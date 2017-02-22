package ua.com.nov.model.entity.database;

import ua.com.nov.model.dao.SqlStatementSource;
import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.entity.table.TableID;

public final class MySqlDb extends Database {
    private static final SqlStatementSource<DatabaseID, Database>
            DATABASE_SQL_STATEMENT_SOURCE = new MySqlDbStmts();
    private static final SqlStatementSource<TableID, Table>
            TABLE_SQL_STATEMENT_SOURCE = new MySqlTableStmts();

    public MySqlDb(DatabaseID pk) {
        super(pk);
    }

    public MySqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public MySqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public MySqlDb(DatabaseID pk, String password) {
        super(pk, password);
    }

    @Override
    public SqlStatementSource<DatabaseID, Database> getSqlStmtSource() {
        return DATABASE_SQL_STATEMENT_SOURCE;
    }

    @Override
    public SqlStatementSource<TableID, Table> getTableSqlStmtSource() {
        return TABLE_SQL_STATEMENT_SOURCE;
    }

    private static class MySqlDbStmts extends AbstractSqlDbStatements {
    }

    private static class MySqlTableStmts extends AbstractSqlTableStatements {
        @Override
        protected void addFullTypeName(Column col, StringBuilder result) {
            result.append(col.getDataType().getTypeName());
            addSizeAndPrecision(col, result);
            if (col.isAutoIncrement()) {
                if (col.getDataType().isAutoIncrement()) result.append(" AUTO_INCREMENT");
                else throw new IllegalArgumentException();
            }
        }
    }
}

