package ua.com.nov.model.dao.service;

import ua.com.nov.model.dao.TableRowMapper;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.GenericTable;

import javax.sql.DataSource;
import java.util.List;

public class CachedRDBMapper<R extends AbstractRow<R>> implements TableRowMapper<R> {
    private final MemoryMapper<R> cachedRows = new MemoryMapper<>();

    private final RDBMapper<R> mapper = new RDBMapper();

    public CachedRDBMapper() {
    }

    @Override
    public GenericTable<R> getTable() {
        return mapper.getTable();
    }

    public void setTable(GenericTable<R> table) {
        mapper.setTable(table);
        cachedRows.setTable(table);
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

    @Override
    public R get(AbstractRow.Id<R> id) throws MappingSystemException {
        R row = (R) cachedRows.get(id);
        if (row == null) {
            row = mapper.get(id);
            cachedRows.add(row);
        }
        return row;
    }

    @Override
    public List<R> getAll() throws MappingSystemException {
        if (cachedRows.size() == mapper.size()) {
            return cachedRows.getAll();
        } else {
            return mapper.getAll();
        }
    }

    @Override
    public List<R> getN(int nStart, int number) throws MappingSystemException {
        if (cachedRows.size() == mapper.size()) {
            return cachedRows.getN(nStart, number);
        } else {
            return mapper.getN(nStart, number);
        }
    }

    @Override
    public List<R> getFetch(FetchParameter... parameters) throws MappingSystemException {
        return mapper.getFetch(parameters);
    }

    @Override
    public R add(R row) throws MappingSystemException {
        return cachedRows.add(mapper.add(row));
    }

    @Override
    public void change(R oldValue, R newValue) throws MappingSystemException {
        mapper.change(oldValue, newValue);
        cachedRows.change(oldValue, newValue);
    }

    @Override
    public void delete(R row) throws MappingSystemException {
        mapper.delete(row);
        cachedRows.delete(row);
    }

    @Override
    public int size() throws MappingSystemException {
        return mapper.size();
    }
}
