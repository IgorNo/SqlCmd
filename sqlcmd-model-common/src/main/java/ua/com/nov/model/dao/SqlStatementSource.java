package ua.com.nov.model.dao;

public interface SqlStatementSource<V, C> {

    String getCreateStmt(V value);
    String getReadOneStmt(V value);
    String getReadNStmt(int nStart, int number, C container);
    String getReadAllStmt(C container);
    String getUpdateStmt(V value);
    String getDeleteStmt(V value);
    String getDeleteAllStmt(C container);
    String getCountStmt(C container);

}
