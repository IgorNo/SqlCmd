package ua.com.nov.model.statement;

public interface SqlStatementSource<V> {

    String getCreateStmt(V value);
    String getReadOneStmt(V value);
    String getReadNStmt(int nStart, int number, V value);
    String getReadAllStmt(V value);
    String getUpdateStmt(V value);
    String getDeleteStmt(V value);
    String getDeleteAllStmt(V value);
    String getCountStmt(V value);

}
