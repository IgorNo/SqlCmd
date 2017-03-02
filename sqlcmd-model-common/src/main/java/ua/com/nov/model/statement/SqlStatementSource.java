package ua.com.nov.model.statement;

public interface SqlStatementSource<K,V,C> {

    String getCreateStmt(V value);
    String getReadOneStmt(K key);
    String getReadNStmt(int nStart, int number, C container);
    String getReadAllStmt(C container);
    String getUpdateStmt(V value);
    String getDeleteStmt(K key);
    String getDeleteAllStmt(C container);
    String getCountStmt(C container);

}
