package ua.com.nov.model.entity.database;

import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.table.TableId;
import ua.com.nov.model.statement.AbstractSqlDbStatements;
import ua.com.nov.model.statement.AbstractSqlTableStatements;

public class HyperSqlDb extends Database {
    private static final HyperSqlDbStmts DATABASE_SQL_STATEMENT_SOURCE = new HyperSqlDbStmts();
    private static final HyperSqlTableStmts TABLE_SQL_STATEMENT_SOURCE = new HyperSqlTableStmts();

    public HyperSqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public HyperSqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public HyperSqlDb(String dbUrl, String userName, String password, String dbProperties) {
        super(dbUrl, userName, password, dbProperties);
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
        if (id.getSchema() != null) result.append(id.getSchema()).append('.');
        return result.append(id.getName()).toString();
    }

    @Override
    public String convert(String parameter) {
        if (parameter != null) return parameter.toUpperCase();
        return parameter;
    }
    @Override
    public String getDbProperties() {
        return "";
    }

    private static class HyperSqlDbStmts extends AbstractSqlDbStatements {
        @Override
        public String getCreateStmt(Database db) {
           throw new UnsupportedOperationException();
        }

        @Override
        public String getDeleteStmt(DatabaseId id) {
            throw new UnsupportedOperationException();
        }
    }

    private static class HyperSqlTableStmts extends AbstractSqlTableStatements {
        @Override
        protected void addFullTypeName(Column col, StringBuilder result) {
            result.append(col.getDataType().getTypeName());
            addSizeAndPrecision(col, result);
            if (col.isAutoIncrement()) {
                if (col.getDataType().isAutoIncrement()) result.append(" GENERATED BY DEFAULT AS IDENTITY");
                else throw new IllegalArgumentException("This type can't be autoincrement");
            }
        }
    }
}
