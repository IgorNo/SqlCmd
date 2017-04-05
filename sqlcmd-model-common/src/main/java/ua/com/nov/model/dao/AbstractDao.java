package ua.com.nov.model.dao;

import org.springframework.jdbc.core.RowMapper;
import ua.com.nov.model.dao.exception.DaoBusinesException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.fetch.FetchParametersSource;
import ua.com.nov.model.dao.statement.SqlStatement;
import ua.com.nov.model.dao.statement.SqlStatementSource;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.database.Database;

import javax.sql.DataSource;
import java.util.List;

public abstract class AbstractDao<K extends Persistent<C>, V extends Unique<K>, C extends Persistent>
        implements Dao<K,V,C> {

    private DataSource dataSource;
    private SqlExecutor<V> executor;

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
    public AbstractDao<K,V,C> setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.executor = getExecutor(dataSource);
        return this;
    }

    protected abstract SqlExecutor getExecutor(DataSource dataSource);

    @Override
    public void create(V value)  throws DaoSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(value.getId().getDb()).getCreateStmt(value));
    }

    protected abstract SqlStatementSource<K,V,C> getSqlStmtSource(Database db);

    @Override
    public void update(V value) throws DaoSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(value.getId().getDb()).getUpdateStmt(value));
    }

    @Override
    public void delete(K key) throws DaoSystemException {
        executor.executeUpdateStmt(getSqlStmtSource(key.getDb()).getDeleteStmt(key));
    }

    protected abstract AbstractRowMapper<V,C> getRowMapper(C id);

    @Override
    public V read(K key) throws DaoSystemException, DaoBusinesException {
        SqlStatement sqlStmt = getSqlStmtSource(key.getDb()).getReadOneStmt(key);
        return executor.executeQueryStmt(sqlStmt, getRowMapper(key.getContainerId())).get(0);
    }

    @Override
    public <T extends FetchParametersSource<C>> List<V> readFetch(T params) throws DaoSystemException {
        SqlStatement sqlStmt = getSqlStmtSource(params.getContainerId().getDb()).getReadFetchStmt(params);
        return executor.executeQueryStmt(sqlStmt, getRowMapper(params.getContainerId()));
    }

    @Override
    public List<V> readAll(C cId)  throws DaoSystemException{
        SqlStatement sqlStmt = getSqlStmtSource(cId.getDb()).getReadAllStmt(cId);
        return executor.executeQueryStmt(sqlStmt, getRowMapper(cId));
    }

    protected abstract static class AbstractRowMapper<V,C> implements RowMapper<V> {
        public AbstractRowMapper(C id) { }
    }

}
