package ua.com.nov.model.dao.impl;

import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.DataDefinitionDao;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.BusinessLogicException;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.dao.statement.DataDefinitionSqlStmtSource;
import ua.com.nov.model.entity.MetaDataId;
import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.server.Server;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class MetaDataDao<I extends AbstractMetaData.Id<C>, E extends AbstractMetaData<I>, C extends MetaDataId>
        extends AbstractDao<I, E, C> implements DataDefinitionDao<I, E> {

    public MetaDataDao() {
    }

    public MetaDataDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected SqlExecutor createExecutor(DataSource dataSource) {
        return new DDLSqlExecutor(dataSource);
    }

    @Override
    protected abstract DataDefinitionSqlStmtSource<I, E, C> getSqlStmtSource(Server db);

    @Override
    public void createIfNotExist(E entity) throws DAOSystemException {
        getExecutor().executeUpdateStmt(getSqlStmtSource(entity.getId().getServer()).getCreateIfNotExistsStmt(entity));
    }

    @Override
    public void deleteIfExist(I eId) throws DAOSystemException {
        getExecutor().executeUpdateStmt(getSqlStmtSource(eId.getServer()).getDeleteIfExistStmt(eId));
    }

    @Override
    public void rename(E entity, String newName) throws DAOSystemException {
        getExecutor().executeUpdateStmt(getSqlStmtSource(entity.getServer()).getRenameStmt(entity, newName));
    }

    @Override
    public E read(I eId) throws DAOSystemException, BusinessLogicException {
        List<E> result;
        try (ResultSet rs =
                     getResultSet(eId.getContainerId(), eId.getName())){
            result = new RowMapperResultSetExtractor<>(getRowMapper(eId.getContainerId())).extractData(rs);
            for (E v : result) {
                if (v.getId().equals(eId)) return v;
            }
            throw new BusinessLogicException(String.format("%s '%s' doesn't exist in %s '%s'.\n",
                    eId.getMdName(), eId.getFullName(),
                    eId.getContainerId().getMdName(), eId.getContainerId().getFullName()));
        } catch (SQLException e) {
            throw new DAOSystemException("MetaDataDao read Exception.\n", e);
        }
    }

    protected abstract ResultSet getResultSet(C containerId, String name)
            throws SQLException;

    @Override
    public List<E> readAll(C cId) throws DAOSystemException {
        List<E> result;
        try (ResultSet rs = getResultSet(cId, null)){
            result = new RowMapperResultSetExtractor<E>(getRowMapper(cId)).extractData(rs);
        } catch (SQLException e) {
            throw new DAOSystemException("MetaDataDao readAll Exception.\n", e);
        }
        return result;
    }

    protected DatabaseMetaData getDbMetaData()  throws SQLException{
        return getDataSource().getConnection().getMetaData();
    }

}
