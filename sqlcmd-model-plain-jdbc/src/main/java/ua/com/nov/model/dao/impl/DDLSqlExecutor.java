package ua.com.nov.model.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.SqlStatement;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DDLSqlExecutor extends SqlExecutor {

    public DDLSqlExecutor(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void executeUpdateStmt(SqlStatement sqlStmt) throws DaoSystemException {
        try (Statement stmt = getDataSource().getConnection().createStatement()){
            String[] sqlArray = sqlStmt.getSql().split(";");
            for (String sql : sqlArray) {
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
           throw new DaoSystemException("DDL DAO update exception.\n", e);
        }
    }

    @Override
    public <T> List<T> executeQueryStmt(SqlStatement sqlStmt, RowMapper<T> mapper) throws DaoSystemException {
        List<T> result;
        try (Statement stmt = getDataSource().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sqlStmt.getSql())) {

            result = new RowMapperResultSetExtractor<T>(mapper).extractData(rs);
        } catch (SQLException e) {
            throw new DaoSystemException("DDL DAO query exception.\n", e);
        }
        return result;
    }


}
