package ua.com.nov.model.dao.impl;

import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.DataDefinitionDao;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.dao.statement.DataDefinitionSqlStmtSource;
import ua.com.nov.model.entity.Hierarchical;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.database.Database;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class MetaDataDao<I extends MetaDataId<C>, E extends MetaData<I>, C extends Hierarchical>
        extends AbstractDao<I,E,C> implements DataDefinitionDao<I, E> {

    @Override
    protected SqlExecutor<E> getExecutor(DataSource dataSource) {
        return new DDLSqlExecutor<>(dataSource);
    }

    @Override
    protected abstract DataDefinitionSqlStmtSource<I, E, C> getSqlStmtSource(Database db);

    @Override
    public void createIfNotExist(E entity) throws DaoSystemException {
        getExecutor().executeUpdateStmt(getSqlStmtSource(entity.getId().getDb()).getCreateIfNotExistsStmt(entity));
    }

    @Override
    public void deleteIfExist(I eId) throws DaoSystemException {
        getExecutor().executeUpdateStmt(getSqlStmtSource(eId.getDb()).getDeleteIfExistStmt(eId));
    }

    @Override
    public void rename(I eId, String newName) throws DaoSystemException {
        getExecutor().executeUpdateStmt(getSqlStmtSource(eId.getDb()).getRenameStmt(eId, newName));
    }

    @Override
    public E read(I eId) throws DaoSystemException, DaoBusinessLogicException {
        List<E> result;
        try (ResultSet rs =
                     getResultSet(eId.getContainerId(), eId.getName())){
            result = new RowMapperResultSetExtractor<>(getRowMapper(eId.getContainerId())).extractData(rs);
            for (E v : result) {
                if (v.getId().equals(eId)) return v;
            }
            throw new DaoBusinessLogicException(String.format("%s '%s' doesn't exist in %s '%s'.\n",
                    eId.getMdName(), eId.getFullName(),
                    eId.getContainerId().getMdName(), eId.getContainerId().getFullName()));
        } catch (SQLException e) {
            throw new DaoSystemException("MetaDataDao read Exception.\n", e);
        }
    }

    protected abstract ResultSet getResultSet(C containerId, String name)
            throws SQLException;

    @Override
    public List<E> readAll(C cId) throws DaoSystemException {
        List<E> result;
        try (ResultSet rs = getResultSet(cId, null)){
            result = new RowMapperResultSetExtractor<E>(getRowMapper(cId)).extractData(rs);
        } catch (SQLException e) {
            throw new DaoSystemException("MetaDataDao readAll Exception.\n", e);
        }
        return result;
    }

    protected DatabaseMetaData getDbMetaData()  throws SQLException{
        return getDataSource().getConnection().getMetaData();
    }

}
