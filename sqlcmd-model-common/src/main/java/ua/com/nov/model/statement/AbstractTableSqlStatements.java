package ua.com.nov.model.statement;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMd;

public abstract class AbstractTableSqlStatements extends BaseSqlStmtSource<TableId, Table, Database.DbId> {
    public static final String CREATE_SQL = "CREATE TABLE %s (%s) %s";
    public static final String DROP_SQL = "DROP TABLE %s";
    public static final String RENAME_SQL = "ALTER TABLE %s RENAME TO %s";

    @Override
    public String getCreateStmt(Table table) {
        return String.format(CREATE_SQL,
                table.getFullName(), getCreateTableDefinition(table), table.getTableProperties());
    }

    @Override
    public String getDeleteStmt(TableId id) {
        return String.format(DROP_SQL, id.getName());
    }

    @Override
    public String getUpdateStmt(Table table) {
        return String.format(RENAME_SQL, table.getId().getName(), table.getNewName());
    }

    private String getCreateTableDefinition(Table table) {
        int numberOfColumns = table.getNumberOfColumns();
        if (numberOfColumns == 0) return "";

        StringBuilder result = new StringBuilder();
        String s = "";
        for (TableMd md : table.getMetaData()) {
            result.append(s).append(md.toString());
            if (s.isEmpty()) s = ",\n";
        }

        return result.toString();
    }
}
