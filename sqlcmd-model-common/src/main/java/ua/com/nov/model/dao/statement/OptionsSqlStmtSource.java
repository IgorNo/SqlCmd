package ua.com.nov.model.dao.statement;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.entity.MetaDataOptions;

public interface OptionsSqlStmtSource<I,E> {
    default SqlStatement getReadOptionsStmt(I eId) {
        return null;
    }

    default SqlStatement getReadAllOptionsStmt() {
        return null;
    }

    default RowMapper<MetaDataOptions.Builder<? extends MetaDataOptions<E>>> getOptionsRowMapper() {
        throw new UnsupportedOperationException();
    }
}
