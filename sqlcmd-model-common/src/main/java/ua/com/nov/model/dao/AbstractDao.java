package ua.com.nov.model.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.dao.exception.NoSuchEntityException;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.dao.statement.SqlStatementSource;
import ua.com.nov.model.entity.Persistance;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.server.Server;

import javax.sql.DataSource;
import java.util.List;

public abstract class AbstractDao<I extends Persistance<C>, E extends Unique<I>, C extends Persistance>
        implements Dao<I, E, C> {

    private SqlExecutor executor;

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
    public void setDataSource(DataSource dataSource) {
        this.executor = createExecutor(dataSource);
    }

    protected abstract SqlExecutor createExecutor(DataSource dataSource);

    protected SqlExecutor getExecutor() {
        return executor;
    }

    @Override
    public void create(E entity) throws DAOSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(entity.getId().getServer()).getCreateStmt(entity));
    }

    protected abstract SqlStatementSource<I, E, C> getSqlStmtSource(Server db);

    @Override
    public void update(E entity) throws DAOSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(entity.getId().getServer()).getUpdateStmt(entity));
    }

    @Override
    public void delete(I eId) throws DAOSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(eId.getServer()).getDeleteStmt(eId));
    }

    protected abstract AbstractRowMapper<E, C> getRowMapper(C id);

    @Override
    public E read(I eId) throws DAOSystemException {
        SqlStatement sqlStmt = getSqlStmtSource(eId.getServer()).getReadOneStmt(eId);
        try {
            E value = executor.executeQueryForObjectStmt(sqlStmt, getRowMapper(eId.getContainerId()));
            return value;
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchEntityException(String.format("Row with id '%s' doesn't exist in '%s'.",
                    eId, eId.getContainerId()), e);
        }
    }

    @Override
    public List<E> readAll(C cId) throws DAOSystemException {
        SqlStatement sqlStmt = getSqlStmtSource(cId.getServer()).getReadAllStmt(cId);
        return executor.executeQueryStmt(sqlStmt, getRowMapper(cId));
    }

    protected abstract static class AbstractRowMapper<V, C> implements RowMapper<V> {
        public AbstractRowMapper(C id) {
        }
    }

}
