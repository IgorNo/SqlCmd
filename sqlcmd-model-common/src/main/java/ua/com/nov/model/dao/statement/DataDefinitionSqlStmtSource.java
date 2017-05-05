package ua.com.nov.model.dao.statement;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.entity.Optional;

public interface DataDefinitionSqlStmtSource<I,E,C> extends SqlStatementSource<I,E,C> {

    SqlStatement getCreateIfNotExistsStmt(E entity);

    SqlStatement getDeleteIfExistStmt(I eId);

    SqlStatement getRenameStmt(I eId, String newName);

    default SqlStatement getReadOptionsStmt(I eId) {
        return null;
    }

    default RowMapper<Optional<E>> getOptionsRowMapper() {
        throw new UnsupportedOperationException();
    }
}
