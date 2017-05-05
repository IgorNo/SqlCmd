package ua.com.nov.model.dao.statement;

public interface SqlStatementSource<I, E, C> {

    SqlStatement getCreateStmt(E entity);

    SqlStatement getUpdateStmt(E entity);

    SqlStatement getDeleteStmt(I eId);

    default SqlStatement getReadOneStmt(I eId) {
        throw new UnsupportedOperationException();
    }

    default SqlStatement getReadAllStmt(C cId){
        throw new UnsupportedOperationException();
    }
}
