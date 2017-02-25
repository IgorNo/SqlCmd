package ua.com.nov.model.dao;

public abstract class BaseSqlStmtSource<V> implements SqlStatementSource<V> {
    @Override
    public String getCreateStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadOneStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadNStmt(int nStart, int number, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadAllStmt(V value) {
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
    public String getDeleteAllStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCountStmt(V value) {
        throw new UnsupportedOperationException();
    }
}
