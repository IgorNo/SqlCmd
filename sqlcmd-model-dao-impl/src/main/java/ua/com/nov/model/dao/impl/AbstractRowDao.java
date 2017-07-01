package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.DataManipulationDao;
import ua.com.nov.model.dao.exception.DaoSystemException;
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

public abstract class AbstractRowDao<R extends Row>
        extends AbstractDao<Row.Id, R, Table> implements DataManipulationDao<R> {

    private Table table;

    public AbstractRowDao() {
    }

    public AbstractRowDao(Table table) {
        setTable(table);
    }

    protected static void setRowValues(ResultSet rs, Row.Builder row, Table table) throws SQLException {
        for (Column column : table.getColumns()) {
            row.setValue(column.getName(), rs.getObject(column.getName()));
        }
    }

    public AbstractRowDao setTable(Table table) {
        this.table = table;
        return this;
    }

    @Override
    public long insert(R value) throws DaoSystemException {
        for (Column column : value.getTable().getColumns()) {
            if (column.isAutoIncrement()) {
                SqlStatement createStmt = getSqlStmtSource(value.getTable().getServer()).getCreateStmt(value);
                return ((DMLSqlExecutor) getExecutor()).executeInsertStmt(createStmt);
            }
        }
        super.create(value);
        return -1;
    }

    public List<R> readAll() throws DaoSystemException {
        return super.readAll(table);
    }

//    @Override
//    public <T extends FetchParametersSource<Table>> List<R> readFetch(T parameters) throws DaoSystemException {
//        SqlStatement sqlStmt = getSqlStmtSource(parameters.getContainerId().getServer()).getReadFetchStmt(parameters);
//        return getExecutor().executeQueryStmt(sqlStmt, getRowMapper(parameters.getContainerId()));
//    }

    @Override
    public List<R> readN(long nStart, int number) throws DaoSystemException {
        SqlStatement sqlStmt = getSqlStmtSource(table.getServer()).getReadNStmt(table, nStart, number);
        return getExecutor().executeQueryStmt(sqlStmt, getRowMapper(table));
    }

    @Override
    public void deleteAll() throws DaoSystemException {
        getExecutor().executeUpdateStmt(getSqlStmtSource(table.getServer()).getDeleteAllStmt(table));
    }

    @Override
    public int count() throws DaoSystemException {
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
}
