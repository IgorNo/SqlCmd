package ua.com.nov.model.statement;

import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;
import ua.com.nov.model.entity.metadata.table.constraint.Constraint;

public abstract class AbstractConstraintSqlStatements<V extends Constraint> extends BaseSqlStmtSource<TableMdId, V , TableId> {
    public static final String CREATE_SQL = "ALTER TABLE %s ADD %s";
    public static final String DROP_SQL = "ALTER TABLE %s DROP CONSTRAINT %s";

    @Override
    public String getCreateStmt(V constraint) {
        return String.format(CREATE_SQL, constraint.getTableId().getFullName(), constraint.toString());
    }

    @Override
    public String getDeleteStmt(TableMdId id) {
        return String.format(DROP_SQL, id.getTableId().getFullName(), id.getName());
    }

}
