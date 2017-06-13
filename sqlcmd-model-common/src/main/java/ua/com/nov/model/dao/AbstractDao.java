package ua.com.nov.model.dao;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.dao.statement.SqlStatementSource;
import ua.com.nov.model.entity.Hierarchical;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.server.Server;

import javax.sql.DataSource;
import java.util.List;

public abstract class AbstractDao<I extends Hierarchical<C>, E extends Unique<I>, C extends Hierarchical>
        implements Dao<I,E,C> {

    private SqlExecutor<E> executor;

    public AbstractDao() {
    }

    public AbstractDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public DataSource getDataSource() {
        return executor.getDataSource();
    }

    @Override
    public AbstractDao<I,E,C> setDataSource(DataSource dataSource) {
        this.executor = getExecutor(dataSource);
        return this;
    }

    protected abstract SqlExecutor<E> getExecutor(DataSource dataSource);

    protected SqlExecutor<E> getExecutor() {
        return executor;
    }

    @Override
    public void create(E value)  throws DaoSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(value.getId().getServer()).getCreateStmt(value));
    }

    protected abstract SqlStatementSource<I, E,C> getSqlStmtSource(Server db);

    @Override
    public void update(E value) throws DaoSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(value.getId().getServer()).getUpdateStmt(value));
    }

    @Override
    public void delete(E entity) throws DaoSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(entity.getId().getServer()).getDeleteStmt(entity));
    }

    protected abstract AbstractRowMapper<E,C> getRowMapper(C id);

    @Override
    public E read(I eId) throws DaoSystemException {
        SqlStatement sqlStmt = getSqlStmtSource(eId.getServer()).getReadOneStmt(eId);
        try {
            return executor.executeQueryStmt(sqlStmt, getRowMapper(eId.getContainerId())).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new DaoBusinessLogicException(String.format("%s '%s' doesn't exist in %s '%s'.\n",
                    eId.getMdName(), eId.getFullName(),
                    eId.getContainerId().getMdName(), eId.getContainerId().getFullName()));
        }

    }

//    @Override
//    public <T extends FetchParametersSource<C>> List<E> readFetch(T params) throws DaoSystemException {
//        SqlStatement sqlStmt = getOptionsSqlStmtSource(params.getContainerId().getServer()).getReadFetchStmt(params);
//        return executor.executeQueryStmt(sqlStmt, getRowMapper(params.getContainerId()));
//    }
//
    @Override
    public List<E> readAll(C cId)  throws DaoSystemException{
        SqlStatement sqlStmt = getSqlStmtSource(cId.getServer()).getReadAllStmt(cId);
        return executor.executeQueryStmt(sqlStmt, getRowMapper(cId));
    }

    protected abstract static class AbstractRowMapper<V,C> implements RowMapper<V> {
        public AbstractRowMapper(C id) { }
    }

}
