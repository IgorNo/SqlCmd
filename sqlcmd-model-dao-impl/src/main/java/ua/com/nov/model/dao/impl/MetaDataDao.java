package ua.com.nov.model.dao.impl;

import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.dao.SqlExecutor;
import ua.com.nov.model.dao.exception.DaoBusinesException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Schematic;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class MetaDataDao
        <K extends MetaDataId<C> & Schematic, V extends MetaData<K>, C extends Persistent & Schematic>
        extends AbstractDao<K,V,C> {

    @Override
    protected SqlExecutor<V> getExecutor(DataSource dataSource) {
        return new DDLSqlExecutor<>(dataSource);
    }

    @Override
    public V read(K key) throws DaoSystemException, DaoBusinesException {
        List<V> result;
        try (ResultSet rs =
                     getResultSet(key.getCatalog(), key.getSchema(), key.getContainerId().getName(), key.getName())){
            result = new RowMapperResultSetExtractor<>(getRowMapper(key.getContainerId())).extractData(rs);
            for (V v : result) {
                if (v.getId().equals(key)) return v;
            }
            throw new DaoBusinesException(String.format("%s '%s' doesn't exist in %s '%s'.\n",
                    key.getMdName(), key.getFullName(),
                    key.getContainerId().getMdName(), key.getContainerId().getFullName()));
        } catch (SQLException e) {
            throw new DaoSystemException("MetaDataDao read Exception.\n", e);
        }
    }

    protected abstract ResultSet getResultSet(String catalog, String schema, String containerName, String name)
            throws SQLException;

    @Override
    public List<V> readAll(C cId) throws DaoSystemException {
        List<V> result;
        try (ResultSet rs = getResultSet(cId.getCatalog(), cId.getSchema(), cId.getName(), null)){
            result = new RowMapperResultSetExtractor<V>(getRowMapper(cId)).extractData(rs);
        } catch (SQLException e) {
            throw new DaoSystemException("MetaDataDao readAll Exception.\n", e);
        }
        return result;
    }

    protected DatabaseMetaData getDbMetaData()  throws SQLException{
        return getDataSource().getConnection().getMetaData();
    }

}
