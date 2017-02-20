package ua.com.nov.model.entity;

import ua.com.nov.model.entity.table.Table;

public interface SqlStatementSource {

    String getCreateStmt();
    String getDeleteStmt();
    String getReadAllStmt();
    String getUpdateStmt();

    String getCreateTableStmt(Table table);
    String getDropTableStmt(Table table);
    String getUpdateTableStmt(Table table);

}
