package ua.com.nov.model.dao.statement;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.entity.Optional;

public interface OptionsSqlStmtSource<I,E> {
    default SqlStatement getReadOptionsStmt(I eId) {
        return null;
    }

    default RowMapper<Optional<E>> getOptionsRowMapper() {
        throw new UnsupportedOperationException();
    }
}
