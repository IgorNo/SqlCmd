package ua.com.nov.model.dao.statement;

import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;

public abstract class AbstractColumnSqlStatements extends BaseSqlStmtSource<Column.Id, Column, Table.Id> {
    public static final String CREATE_SQL = "ALTER TABLE %s ADD COLUMN %s";
    public static final String DROP_SQL = "ALTER TABLE %s DROP COLUMN %s";

    @Override
    public SqlStatement getCreateStmt(Column column) {
        return new SqlStatement.Builder(String.format(CREATE_SQL,
                column.getTableId().getFullName(), column.toString())).build();
    }

    @Override
    public SqlStatement getDeleteStmt(Column.Id id) {
        return new SqlStatement.Builder(String.format(DROP_SQL, id.getTableId().getFullName(),id.getName())).build();
    }
}
