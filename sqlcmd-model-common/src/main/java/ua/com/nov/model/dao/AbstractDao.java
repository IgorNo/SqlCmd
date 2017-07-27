package ua.com.nov.model.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.exception.NoSuchEntityException;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.dao.statement.SqlStatementSource;
import ua.com.nov.model.entity.Hierarchical;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.server.Server;

import javax.sql.DataSource;
import java.util.List;

public abstract class AbstractDao<I extends Hierarchical<C>, E extends Unique<I>, C extends Hierarchical>
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
    public void create(E value) throws MappingSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(value.getId().getServer()).getCreateStmt(value));
    }

    protected abstract SqlStatementSource<I, E, C> getSqlStmtSource(Server db);

    @Override
    public void update(E value) throws MappingSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(value.getId().getServer()).getUpdateStmt(value));
    }

    @Override
    public void delete(E entity) throws MappingSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(entity.getId().getServer()).getDeleteStmt(entity));
    }

    protected abstract AbstractRowMapper<E, C> getRowMapper(C id);

    @Override
    public E read(I eId) throws MappingSystemException {
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
    public List<E> readAll(C cId) throws MappingSystemException {
        SqlStatement sqlStmt = getSqlStmtSource(cId.getServer()).getReadAllStmt(cId);
        return executor.executeQueryStmt(sqlStmt, getRowMapper(cId));
    }

    protected abstract static class AbstractRowMapper<V, C> implements RowMapper<V> {
        public AbstractRowMapper(C id) {
        }
    }

}
