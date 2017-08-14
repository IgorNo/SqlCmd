package ua.com.nov.model.dao.impl;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.DataManipulationDao;
import ua.com.nov.model.dao.exception.MappingBusinessLogicException;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.dao.statement.AbstractDataStmtSource;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.data.Row;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.entity.metadata.table.GenericTable;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RowDao<R extends AbstractRow<R>>
        extends AbstractDao<AbstractRow.Id<R>, R, GenericTable<R>> implements DataManipulationDao<R> {

    public RowDao() {
    }

    public RowDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public KeyHolder insert(R value) throws MappingSystemException {
        for (Column column : value.getTable().getColumns()) {
            if (column.isAutoIncrement()) {
                SqlStatement createStmt = getSqlStmtSource(value.getTable().getServer()).getCreateStmt(value);
                return getExecutor().executeInsertStmt(createStmt);
            }
        }
        super.create(value);
        return null;
    }

    private static void setRowValues(ResultSet rs, AbstractRow.Builder row, Table table) throws SQLException {
        for (Column column : table.getColumns()) {
            row.setValue(column.getName(), rs.getObject(column.getName()));
        }
    }

    @Override
    public List<R> readFetch(GenericTable<R> table, FetchParameter... parameters) throws MappingSystemException {
        SqlStatement sqlStmt = getSqlStmtSource(table.getServer()).getReadFetchStmt(parameters);
        return getExecutor().executeQueryStmt(sqlStmt, getRowMapper(table));
    }

    @Override
    public void deleteAll(Table table) throws MappingSystemException {
        getExecutor().executeUpdateStmt(getSqlStmtSource(table.getServer()).getDeleteAllStmt(table));
    }

    @Override
    public int count(Table table) throws MappingSystemException {
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
    public List<R> readN(GenericTable<R> table, long nStart, int number) throws MappingSystemException {
        SqlStatement sqlStmt = getSqlStmtSource(table.getServer()).getReadNStmt(table, nStart, number);
        return getExecutor().executeQueryStmt(sqlStmt, getRowMapper(table));
    }

    @Override
    protected AbstractRowMapper<R, GenericTable<R>> getRowMapper(GenericTable<R> table) {
        return new AbstractRowMapper<R, GenericTable<R>>(table) {
            @Override
            public R mapRow(ResultSet rs, int rowNum) throws SQLException {
                AbstractRow.Builder<R> row = null;
                if (table.getRowClass() == Row.class) {
                    row = (AbstractRow.Builder<R>) new Row.Builder((GenericTable<Row>) table);
                } else {
                    try {
                        row = (AbstractRow.Builder<R>) Class.forName(table.getRowClass().getName() + "$Builder").newInstance();
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        throw new MappingBusinessLogicException("Create Builder Error.\n", e);
                    }
                }
                setRowValues(rs, row, table);
                return row.build();
            }
        };
    }
}
