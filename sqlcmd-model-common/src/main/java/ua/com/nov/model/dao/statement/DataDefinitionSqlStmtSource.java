package ua.com.nov.model.dao.statement;

public interface DataDefinitionSqlStmtSource<I,E,C> extends SqlStatementSource<I,E,C> {

    SqlStatement getCreateIfNotExistsStmt(E entity);

    SqlStatement getDeleteIfExistStmt(I eId);

    SqlStatement getRenameStmt(E entity, String newName);

}
