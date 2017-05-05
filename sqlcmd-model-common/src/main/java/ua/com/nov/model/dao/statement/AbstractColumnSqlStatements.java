package ua.com.nov.model.dao.statement;

import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;

public abstract class AbstractColumnSqlStatements implements DataDefinitionSqlStmtSource<Column.Id, Column, Table.Id> {

    @Override
    public SqlStatement getCreateStmt(Column entity) {
        return new SqlStatement.Builder(String.format("ALTER TABLE %s ADD %s",
                entity.getTableId().getFullName(), entity.getCreateStmtDefinition(null))).build();
    }

    @Override
    public SqlStatement getCreateIfNotExistsStmt(Column entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getUpdateStmt(Column entity) {
        StringBuilder sql = new StringBuilder();
        if (entity.getViewName() != null) sql.append('\n').append(getCommentStmt(entity)).append(';');
        for (String s : entity.getOptions().getUpdateOptionsDefinition()) {
            sql.append(String.format("\nALTER TABLE %s %s;",  entity.getId().getFullName(), s));
        }
        return new SqlStatement.Builder(sql.toString()).build();
    }

    protected String getCommentStmt(Column column) {
        if (column.getViewName() == null) return "";
        return String.format("COMMENT ON COLUMN %s IS '%s'", column.getId().getFullName(), column.getViewName());
    }

    @Override
    public SqlStatement getDeleteStmt(Column.Id eId) {
        return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP COLUMN %s",
                eId.getTableId().getFullName(), eId.getName())).build();
    }

    @Override
    public SqlStatement getDeleteIfExistStmt(Column.Id eId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getRenameStmt(Column.Id eId, String newName) {
        throw new UnsupportedOperationException();
//        return new SqlStatement.Builder(String.format("ALTER TABLE %s ALTER COLUMN %s RENAME TO %s",
//                eId.getTableId().getFullName(), eId.getName())).build();
    }
}
