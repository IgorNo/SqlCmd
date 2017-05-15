package ua.com.nov.model.dao.statement;

public interface DataDefinitionSqlStmtSource<I,E,C> extends SqlStatementSource<I,E,C> {

    SqlStatement getCreateIfNotExistsStmt(E entity);

    SqlStatement getDeleteIfExistStmt(E entity);

    SqlStatement getRenameStmt(E entity, String newName);

}
