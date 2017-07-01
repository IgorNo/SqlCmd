package ua.com.nov.model.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SqlParameterValue;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.SqlStatement;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DMLSqlExecutor extends SqlExecutor {

    public DMLSqlExecutor(DataSource dataSource) {
        super(dataSource);
    }

    private static void setParameters(SqlStatement sqlStmt, PreparedStatement stmt) throws SQLException {
        List<SqlParameterValue> parameters = sqlStmt.getParameters();
        for (int i = 1; i <= parameters.size(); i++) {
            SqlParameterValue parameter = parameters.get(i - 1);
            if (parameter != null) {
                stmt.setObject(i, parameter.getValue(), parameter.getSqlType());
            } else
                stmt.setNull(i, parameter.getSqlType());
        }
    }

    @Override
    public void executeUpdateStmt(SqlStatement sqlStmt) throws DaoSystemException {
        try (PreparedStatement stmt = getDataSource().getConnection().prepareStatement(sqlStmt.getSql())) {
            setParameters(sqlStmt, stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoSystemException("DML DAO update exception.\n", e);
        }
    }

    public long executeInsertStmt(SqlStatement sqlStmt) throws DaoSystemException {
        try (PreparedStatement stmt = getDataSource().getConnection()
                .prepareStatement(sqlStmt.getSql(), Statement.RETURN_GENERATED_KEYS)) {

            setParameters(sqlStmt, stmt);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getLong(1);
            return -1;
        } catch (SQLException e) {
            throw new DaoSystemException("DML DAO insert exception.\n", e);
        }
    }

    @Override
    public <T> List<T> executeQueryStmt(SqlStatement sqlStmt, RowMapper<T> mapper) throws DaoSystemException {
        List<T> result;
        try (PreparedStatement stmt = getDataSource().getConnection().prepareStatement(sqlStmt.getSql())) {
            setParameters(sqlStmt, stmt);
            ResultSet rs = stmt.executeQuery();
            result = new RowMapperResultSetExtractor<T>(mapper).extractData(rs);
        } catch (SQLException e) {
            throw new DaoSystemException("DDL DAO query exception.\n", e);
        }
        return result;
    }

}
