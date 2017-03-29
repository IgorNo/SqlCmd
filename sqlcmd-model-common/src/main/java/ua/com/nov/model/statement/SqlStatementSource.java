package ua.com.nov.model.statement;

public interface SqlStatementSource<K,V,C> {

    SqlStatement getCreateStmt(V value);
    SqlStatement getReadOneStmt(K key);
    SqlStatement getReadNStmt(int nStart, int number, C container);
    SqlStatement getReadAllStmt(C container);
    SqlStatement getUpdateStmt(V value);
    SqlStatement getDeleteStmt(K key);
    SqlStatement getDeleteAllStmt(C container);
    SqlStatement getCountStmt(C container);

}
