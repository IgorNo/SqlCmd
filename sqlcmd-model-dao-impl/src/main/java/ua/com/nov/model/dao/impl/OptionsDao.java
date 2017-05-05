package ua.com.nov.model.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import ua.com.nov.model.dao.statement.DataDefinitionSqlStmtSource;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.Hierarchical;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.MetaDataId;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class OptionsDao<I extends MetaDataId, E> {

    public Optional<E> read(I eId, DataSource dataSource) throws SQLException {
        DataDefinitionSqlStmtSource stmtSource = eId.getDb().getSqlStmtSource(eId.getMdName());
        SqlStatement sqlStmt = stmtSource.getReadOptionsStmt(eId);
        if (sqlStmt != null) {
            List<Optional<E>> result;
            Statement stmt = dataSource.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sqlStmt.getSql());
            result = new RowMapperResultSetExtractor<Optional<E>>(stmtSource.getOptionsRowMapper()).extractData(rs);
            return result.get(0);
        }
        return null;
    }

    private RowMapper getRowMapper(Hierarchical containerId) {
        return null;
    }

}
