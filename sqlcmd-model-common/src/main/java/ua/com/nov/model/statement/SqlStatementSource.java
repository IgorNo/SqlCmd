package ua.com.nov.model.statement;

public interface SqlStatementSource<K,V> {

    String getCreateStmt(V value);
    String getReadOneStmt(K key);
    String getReadNStmt(int nStart, int number, K key);
    String getReadAllStmt(K key);
    String getUpdateStmt(V value);
    String getDeleteStmt(K key);
    String getDeleteAllStmt(K key);
    String getCountStmt(K key);

}
