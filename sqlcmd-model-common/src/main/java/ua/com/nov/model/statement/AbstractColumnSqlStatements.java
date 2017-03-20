package ua.com.nov.model.statement;

import ua.com.nov.model.entity.metadata.table.*;

public abstract class AbstractColumnSqlStatements extends BaseSqlStmtSource<TableMdId, Column, TableId> {
    public static final String CREATE_SQL = "ALTER TABLE %s ADD COLUMN %s";
    public static final String DROP_SQL = "ALTER TABLE %s DROP COLUMN %s";

    @Override
    public String getCreateStmt(Column column) {
        return String.format(CREATE_SQL, column.getTableId().getFullName(), column.toString());
    }

    @Override
    public String getDeleteStmt(TableMdId id) {
        return String.format(DROP_SQL, id.getTableId().getFullName(),id.getName());
    }
}
