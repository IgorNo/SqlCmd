package ua.com.nov.model.entity.database;

import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.table.TableId;

public final class MySqlDb extends Database {
    private static final HyperSqlDbStmts DATABASE_SQL_STATEMENT_SOURCE = new HyperSqlDbStmts();
    private static final HyperSqlTableStmts TABLE_SQL_STATEMENT_SOURCE = new HyperSqlTableStmts();

    public MySqlDb(DatabaseId pk) {
        super(pk);
    }

    public MySqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public MySqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public MySqlDb(DatabaseId pk, String password) {
        super(pk, password);
    }

    @Override
    public HyperSqlDbStmts getSqlStmtSource() {
        return DATABASE_SQL_STATEMENT_SOURCE;
    }

    @Override
    public HyperSqlTableStmts getTableSqlStmtSource() {
        return TABLE_SQL_STATEMENT_SOURCE;
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

    private static class HyperSqlDbStmts extends AbstractSqlDbStatements {
    }

    private static class HyperSqlTableStmts extends AbstractSqlTableStatements {
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

