package ua.com.nov.model.dao.impl;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.util.JdbcUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class DMLSqlExecutor extends SqlExecutor {

    public DMLSqlExecutor(DataSource dataSource) {
        super(dataSource);
    }

    private static void setParameters(SqlStatement sqlStmt, PreparedStatement stmt) throws SQLException {
        List<SqlParameterValue> parameters = sqlStmt.getSqlParameterValues();
        for (int i = 1; i <= parameters.size(); i++) {
            SqlParameterValue parameter = parameters.get(i - 1);
            if (parameter != null) {
                stmt.setObject(i, parameter.getValue(), parameter.getSqlType());
            } else
                stmt.setNull(i, parameter.getSqlType());
        }
    }

    @Override
    public void executeUpdateStmt(SqlStatement sqlStmt) throws MappingSystemException {
        if (sqlStmt != null) {
            try (PreparedStatement stmt = getDataSource().getConnection().prepareStatement(sqlStmt.getSql())) {
                setParameters(sqlStmt, stmt);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new MappingSystemException("DML DAO update exception.\n", e);
            }
        }
    }

    @Override
    public KeyHolder executeInsertStmt(SqlStatement sqlStmt) throws MappingSystemException {
        ResultSet keys = null;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try (PreparedStatement stmt = getDataSource().getConnection()
                .prepareStatement(sqlStmt.getSql(), Statement.RETURN_GENERATED_KEYS)) {

            setParameters(sqlStmt, stmt);
            stmt.executeUpdate();

            keys = stmt.getGeneratedKeys();
            if (keys != null) {
                RowMapper<Map<String, Object>> columnMapper = new ColumnMapRowMapper();
                RowMapperResultSetExtractor<Map<String, Object>> rse =
                        new RowMapperResultSetExtractor<Map<String, Object>>(columnMapper, 1);
                List<Map<String, Object>> generatedKeys = keyHolder.getKeyList();
                generatedKeys.addAll(rse.extractData(keys));
            }
        } catch (SQLException e) {
            throw new MappingSystemException("DML DAO insert exception.\n", e);
        } finally {
            JdbcUtils.closeQuietly(keys);
        }
        return keyHolder;
    }

    @Override
    public <T> List<T> executeQueryStmt(SqlStatement sqlStmt, RowMapper<T> mapper) throws MappingSystemException {
        ResultSet rs = null;
        try (PreparedStatement stmt = getDataSource().getConnection().prepareStatement(sqlStmt.getSql())) {
            setParameters(sqlStmt, stmt);
            rs = stmt.executeQuery();
            return new RowMapperResultSetExtractor<T>(mapper).extractData(rs);
        } catch (SQLException e) {
            throw new MappingSystemException("DDL DAO query exception.\n", e);
        } finally {
            JdbcUtils.closeQuietly(rs);
        }
    }

}
