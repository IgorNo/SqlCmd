package ua.com.nov.model.dao.statement;

import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;

public abstract class AbstractTableMdSqlStatements<I extends TableMd.Id, E extends TableMd>
        implements DataDefinitionSqlStmtSource<I, E,  Table.Id> {

    @Override
    public SqlStatement getCreateStmt(E entity) {
        return new SqlStatement.Builder(String.format("ALTER TABLE %s ADD %s",
                entity.getTableId().getFullName(), entity.getCreateStmtDefinition(null))).build();
    }

    @Override
    public SqlStatement getCreateIfNotExistsStmt(E entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getUpdateStmt(E entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getDeleteStmt(E entity) {
        return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP %s %s",
                entity.getTableId().getFullName(), entity.getId().getMdName(), entity.getName())).build();
    }

    @Override
    public SqlStatement getDeleteIfExistStmt(E entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getRenameStmt(E entity, String newName) {
        return new SqlStatement.Builder(String.format("ALTER TABLE %s RENAME %s %s TO %s",
                entity.getTableId().getFullName(), entity.getId().getMdName(), entity.getName(), newName)).build();
    }
}
