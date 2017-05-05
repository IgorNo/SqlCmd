package ua.com.nov.model.dao.statement;

import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.constraint.Constraint;

public abstract class AbstractConstraintSqlStatements<I extends Constraint.Id, E extends Constraint<I>>
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
    public SqlStatement getDeleteStmt(I eId) {
        return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP CONSTRAINT %s",
                eId.getTableId().getFullName(), eId.getName())).build();
    }

    @Override
    public SqlStatement getDeleteIfExistStmt(I eId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getRenameStmt(I eId, String newName) {
        return new SqlStatement.Builder(String.format("ALTER TABLE %s RENAME CONSTRAINT %s TO %s",
                eId.getTableId().getFullName(), eId.getName(), newName)).build();
    }
}
