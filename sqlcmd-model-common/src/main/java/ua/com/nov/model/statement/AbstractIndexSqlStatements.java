package ua.com.nov.model.statement;

import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;

public abstract class AbstractIndexSqlStatements extends BaseSqlStmtSource<TableMdId, Index, TableId>  {
    public static final String CREATE_SQL = "CREATE  %s";
    public static final String DROP_SQL = "DROP INDEX %s";

    @Override
    public String getCreateStmt(Index index) {
        return String.format(CREATE_SQL, index.toString());
    }

    @Override
    public String getDeleteStmt(TableMdId id) {
        return String.format(DROP_SQL, id.getName());
    }

}
