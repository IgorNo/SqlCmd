package ua.com.nov.model.dao.impl;

import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import ua.com.nov.model.dao.statement.OptionsSqlStmtSource;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.MetaDataId;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class OptionsDao<I extends MetaDataId, E> {

    private DataSource dataSource;

    public OptionsDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<E> read(I eId) throws SQLException {
        OptionsSqlStmtSource<I,E> stmtSource = eId.getDb().getOptionsSqlStmtSource(eId.getMdName());
        SqlStatement sqlStmt = stmtSource.getReadOptionsStmt(eId);
        if (sqlStmt != null) {
            List<Optional<E>> result;
            Statement stmt = dataSource.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sqlStmt.getSql());
            result = new RowMapperResultSetExtractor<>(stmtSource.getOptionsRowMapper()).extractData(rs);
            return result.get(0);
        }
        return null;
    }
}