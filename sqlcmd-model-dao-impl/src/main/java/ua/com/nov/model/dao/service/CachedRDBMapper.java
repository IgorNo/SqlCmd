package ua.com.nov.model.dao.service;

import ua.com.nov.model.dao.TableRowMapper;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.GenericTable;

import javax.sql.DataSource;
import java.util.ConcurrentModificationException;
import java.util.List;

public class CachedRDBMapper<R extends AbstractRow<R>> implements TableRowMapper<R> {
    private final MemoryMapper<R> cachedRows = new MemoryMapper<>();
    private final RDBMapper<R> mapper = new RDBMapper();

    public CachedRDBMapper() {
    }

    public CachedRDBMapper(GenericTable<R> table) {
        setTable(table);
    }

    @Override
    public GenericTable<R> getTable() {
        return mapper.getTable();
    }

    public void setTable(GenericTable<R> table) {
        mapper.setTable(table);
        cachedRows.setTable(table);
    }

    public DataSource getDataSource() {
        return mapper.getDataSource();
    }

    public void setDataSource(DataSource dataSource) {
        mapper.setDataSource(dataSource);
    }

    public boolean isImmediateInitialization() {
        return mapper.isImmediateInitialization();
    }

    public void setImmediateInitialization(boolean immediateInitialization) {
        mapper.setImmediateInitialization(immediateInitialization);
    }

    public void clearCache() {
        MemoryMapper.clear();
    }

    @Override
    public R get(AbstractRow.Id<R> id) throws DAOSystemException {
        R row = (R) cachedRows.get(id);
        if (row == null) {
            row = mapper.get(id);
            cachedRows.add(row);
        }
        return row;
    }

    @Override
    public List<R> getAll() throws DAOSystemException {
        if (cachedRows.size() == mapper.size()) {
            return cachedRows.getAll();
        } else {
            List<R> rows = mapper.getAll();
            for (R row : rows) {
                try {
                    cachedRows.add(row);
                } catch (ConcurrentModificationException e) { /*NOP*/}
            }
            return rows;
        }
    }

    @Override
    public List<R> getN(int nStart, int number) throws DAOSystemException {
        if (cachedRows.size() == mapper.size()) {
            return cachedRows.getN(nStart, number);
        } else {
            return mapper.getN(nStart, number);
        }
    }

    @Override
    public List<R> getFetch(FetchParameter... parameters) throws DAOSystemException {
        return mapper.getFetch(parameters);
    }

    @Override
    public R add(R row) throws DAOSystemException {
        R rowWithId = mapper.add(row);
        cachedRows.add(rowWithId);
        return rowWithId;
    }

    @Override
    public void change(R oldValue, R newValue) throws DAOSystemException {
        mapper.change(oldValue, newValue);
        if (cachedRows.get(oldValue.getId()) != null)
            cachedRows.change(oldValue, newValue);
        else
            cachedRows.add(newValue);
    }

    @Override
    public void delete(R row) throws DAOSystemException {
        mapper.delete(row);
        if (cachedRows.get(row.getId()) != null) cachedRows.delete(row);
    }

    @Override
    public void deleteAll() throws DAOSystemException {
        mapper.deleteAll();
        cachedRows.deleteAll();
    }

    @Override
    public int size() throws DAOSystemException {
        return mapper.size();
    }
}
