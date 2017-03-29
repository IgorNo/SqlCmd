package ua.com.nov.model.statement;

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
    public SqlStatement getReadNStmt(int nStart, int number, C containerId) {
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
