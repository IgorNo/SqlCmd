package ua.com.nov.model.dao;

public abstract class BaseSqlStmtSource<V, C> implements SqlStatementSource<V, C> {
    @Override
    public String getCreateStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadOneStmt(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadNStmt(int nStart, int number, C container) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getReadAllStmt(C container) {
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
    public String getDeleteAllStmt(C container) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCountStmt(C container) {
        throw new UnsupportedOperationException();
    }
}
