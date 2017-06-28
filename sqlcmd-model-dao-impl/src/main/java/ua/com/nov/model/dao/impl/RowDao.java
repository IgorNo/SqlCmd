package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.DataManipulationDao;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.fetch.FetchParametersSource;
import ua.com.nov.model.dao.statement.AbstractDataStmtSource;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.data.Row;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RowDao
        extends AbstractDao<Row.Id, Row, Table> implements DataManipulationDao<Row, Table> {

    @Override
    public long insert(Row value) throws DaoSystemException {
        for (Column column : value.getTable().getColumns()) {
            if (column.isAutoIncrement()) {
                SqlStatement createStmt = getSqlStmtSource(value.getTable().getServer()).getCreateStmt(value);
                return ((DMLSqlExecutor) getExecutor()).executeInsertStmt(createStmt);
            }
        }
        super.create(value);
        return -1;
    }

    @Override
    public List<Row> readN(Table table, long nStart, int number) throws DaoSystemException {
        SqlStatement sqlStmt = getSqlStmtSource(table.getServer()).getReadNStmt(table, nStart, number);
        return getExecutor().executeQueryStmt(sqlStmt, getRowMapper(table));
    }

    @Override
    public <T extends FetchParametersSource<Table>> List<Row> readFetch(T parameters) throws DaoSystemException {
        SqlStatement sqlStmt = getSqlStmtSource(parameters.getContainerId().getServer()).getReadFetchStmt(parameters);
        return getExecutor().executeQueryStmt(sqlStmt, getRowMapper(parameters.getContainerId()));
    }

    @Override
    public void deleteAll(Table table) throws DaoSystemException {
        getExecutor().executeUpdateStmt(getSqlStmtSource(table.getServer()).getDeleteAllStmt(table));
    }

    @Override
    public int count(Table table) throws DaoSystemException {
        return getExecutor()
                .executeQueryForObjectStmt(getSqlStmtSource(table.getServer()).getCountStmt(table), Integer.class);
    }

    @Override
    protected DMLSqlExecutor createExecutor(DataSource dataSource) {
        return new DMLSqlExecutor(dataSource);
    }

    @Override
    protected AbstractDataStmtSource getSqlStmtSource(Server server) {
        return server.getDataSqlStmtSource();
    }

    @Override
    protected AbstractRowMapper<Row, Table> getRowMapper(Table table) {
        return new AbstractRowMapper<Row, Table>(table) {
            @Override
            public Row mapRow(ResultSet rs, int i) throws SQLException {
                Row.Builder row = new Row.Builder(table);
                for (Column column : table.getColumns()) {
                    row.setValue(column.getName(), rs.getObject(column.getName()));
                }
                return null;
            }
        };
    }
}
