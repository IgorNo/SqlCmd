package ua.com.nov.model.entity.database;

import ua.com.nov.model.entity.table.Table;

public interface Executable {

    String getCreateDbStmt();
    String getDropDbStmt();
    String getSelectAllDbStmt();
    String getAlterDbStmt();

    String getCreateTableStmt(Table table);
    String getDropTableStmt(Table table);
    String getUpdateTableStmt(Table table);

}
