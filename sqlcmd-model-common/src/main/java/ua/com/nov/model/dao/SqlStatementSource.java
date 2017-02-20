package ua.com.nov.model.dao;

public interface SqlStatementSource<K, V> {

    String getCreateStmt(V value);
    String getReadOneStmt(K key);
    String getReadNStmt(int nStart, int number);
    String getReadAllStmt();
    String getUpdateStmt(V value);
    String getDeleteStmt(V value);
    String getDeleteAllStmt();
    String getCountStmt();

}
