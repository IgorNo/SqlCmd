package ua.com.nov.model.statement;

public abstract class BaseSqlStmtSource<K,V> implements SqlStatementSource<K,V> {
    @Override
    public String getCreateStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadOneStmt(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadNStmt(int nStart, int number, K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadAllStmt(K key) {
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
    public String getDeleteAllStmt(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCountStmt(K key) {
        throw new UnsupportedOperationException();
    }
}
