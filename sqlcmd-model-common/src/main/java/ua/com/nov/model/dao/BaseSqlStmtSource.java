package ua.com.nov.model.dao;

public abstract class BaseSqlStmtSource<K, V> implements SqlStatementSource<K, V> {
    @Override
    public String getCreateStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadOneStmt(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadNStmt(int nStart, int number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadAllStmt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getUpdateStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDeleteStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDeleteAllStmt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCountStmt() {
        throw new UnsupportedOperationException();
    }
}
