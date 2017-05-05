package ua.com.nov.model.dao;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.dao.statement.SqlStatementSource;
import ua.com.nov.model.entity.Hierarchical;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.database.Database;

import javax.sql.DataSource;
import java.util.List;

public abstract class AbstractDao<I extends Hierarchical<C>, E extends Unique<I>, C extends Hierarchical>
        implements Dao<I, E,C> {

    private DataSource dataSource;
    private SqlExecutor<E> executor;

    public AbstractDao() {
    }

    public AbstractDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public AbstractDao<I,E,C> setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.executor = getExecutor(dataSource);
        return this;
    }

    protected abstract SqlExecutor<E> getExecutor(DataSource dataSource);

    protected SqlExecutor<E> getExecutor() {
        return executor;
    }

    @Override
    public void create(E value)  throws DaoSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(value.getId().getDb()).getCreateStmt(value));
    }

    protected abstract SqlStatementSource<I, E,C> getSqlStmtSource(Database db);

    @Override
    public void update(E value) throws DaoSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(value.getId().getDb()).getUpdateStmt(value));
    }

    @Override
    public void delete(I key) throws DaoSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(key.getDb()).getDeleteStmt(key));
    }

    protected abstract AbstractRowMapper<E,C> getRowMapper(C id);

    @Override
    public E read(I key) throws DaoSystemException {
        SqlStatement sqlStmt = getSqlStmtSource(key.getDb()).getReadOneStmt(key);
        return executor.executeQueryStmt(sqlStmt, getRowMapper(key.getContainerId())).get(0);
    }

//    @Override
//    public <T extends FetchParametersSource<C>> List<E> readFetch(T params) throws DaoSystemException {
//        SqlStatement sqlStmt = getSqlStmtSource(params.getContainerId().getDb()).getReadFetchStmt(params);
//        return executor.executeQueryStmt(sqlStmt, getRowMapper(params.getContainerId()));
//    }
//
    @Override
    public List<E> readAll(C cId)  throws DaoSystemException{
        SqlStatement sqlStmt = getSqlStmtSource(cId.getDb()).getReadAllStmt(cId);
        return executor.executeQueryStmt(sqlStmt, getRowMapper(cId));
    }

    protected abstract static class AbstractRowMapper<V,C> implements RowMapper<V> {
        public AbstractRowMapper(C id) { }
    }

}
