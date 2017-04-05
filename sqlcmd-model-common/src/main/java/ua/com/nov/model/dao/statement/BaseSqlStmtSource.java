package ua.com.nov.model.dao.statement;

import ua.com.nov.model.dao.fetch.FetchParametersSource;

public abstract class BaseSqlStmtSource<K,V,C> implements SqlStatementSource<K,V,C> {
    @Override
    public SqlStatement getCreateStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getReadOneStmt(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getReadNStmt(C containerId, int nStart, int number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends FetchParametersSource<C>> SqlStatement getReadFetchStmt(T parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getReadAllStmt(C containerId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getUpdateStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getDeleteStmt(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getDeleteAllStmt(C containerId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SqlStatement getCountStmt(C containerId) {
        throw new UnsupportedOperationException();
    }
}
