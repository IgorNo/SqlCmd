package ua.com.nov.model.statement;

public abstract class BaseSqlStmtSource<K,V,C> implements SqlStatementSource<K,V,C> {
    @Override
    public String getCreateStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadOneStmt(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadNStmt(int nStart, int number, C containerId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadAllStmt(C containerId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getUpdateStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDeleteStmt(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDeleteAllStmt(C containerId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCountStmt(C containerId) {
        throw new UnsupportedOperationException();
    }
}
