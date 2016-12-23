package ua.com.nov.model.statement;

public interface Executable {

    String getCreateDbStmt();
    String getDropDbStmt();
    String getSelectAllDbStmt();
    String getAlterDbStmt();
    String getCreateTableStmt();
    String getDropTableStmt();
    String getUpdateTableStmt();
}
