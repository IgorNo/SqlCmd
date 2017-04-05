package ua.com.nov.model.dao.statement;

import ua.com.nov.model.dao.fetch.FetchParametersSource;

public interface SqlStatementSource<K,V,C> {
    SqlStatement getCreateStmt(V value);
    SqlStatement getReadOneStmt(K key);
    SqlStatement getReadNStmt(C container, int nStart, int number);
    <T extends FetchParametersSource<C>> SqlStatement getReadFetchStmt(T parameters);
    SqlStatement getReadAllStmt(C container);
    SqlStatement getUpdateStmt(V value);
    SqlStatement getDeleteStmt(K key);
    SqlStatement getDeleteAllStmt(C container);
    SqlStatement getCountStmt(C container);
}
