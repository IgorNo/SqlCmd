package ua.com.nov.model.statement;

import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.constraint.Constraint;

public abstract class AbstractConstraintSqlStatements<K extends Constraint.Id, V extends Constraint>
        extends BaseSqlStmtSource<K, V , Table.Id> {

    @Override
    public SqlStatement getCreateStmt(V constraint) {
        return new SqlStatement.Builder(String.format("ALTER TABLE %s ADD %s",
                constraint.getTableId().getFullName(), constraint.toString())).build();
    }

    @Override
    public SqlStatement getDeleteStmt(K id) {
        return new SqlStatement.Builder(String.format("ALTER TABLE %s DROP CONSTRAINT %s",
                id.getTableId().getFullName(), id.getName())).build();
    }

    @Override
    public SqlStatement getUpdateStmt(V value) {
        return new SqlStatement.Builder(String.format("ALTER TABLE %s RENAME CONSTRAINT %s TO %s",
                value.getTableId().getFullName(), value.getName(), value.getNewName())).build();
    }

}
