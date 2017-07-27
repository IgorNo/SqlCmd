package ua.com.nov.model.dao.service;

import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.Table;

import javax.sql.DataSource;
import java.util.List;

public class CachedRDBMapper implements Mapper {
    private static final MemoryMapper cachedRows = new MemoryMapper();

    private RDBMapper mapper = new RDBMapper();

    public void setDataSource(DataSource dataSource) {
        mapper.setDataSource(dataSource);
    }

    @Override
    public <T extends AbstractRow> T get(AbstractRow.Id id) throws MappingSystemException {
        T row = cachedRows.get(id);
        if (row == null) {
            row = mapper.get(id);
            cachedRows.add(row);
        }
        return row;
    }

    @Override
    public <R extends AbstractRow> List<R> getAll(Table table) throws MappingSystemException {
        if (cachedRows.size(table) == mapper.size(table)) {
            return cachedRows.getAll(table);
        } else {
            return mapper.getAll(table);
        }
    }

    @Override
    public <R extends AbstractRow> List<R> getN(Table table, int nStart, int number) throws MappingSystemException {
        if (cachedRows.size(table) == mapper.size(table)) {
            return cachedRows.getN(table, nStart, number);
        } else {
            return mapper.getN(table, nStart, number);
        }
    }

    @Override
    public <R extends AbstractRow> List<R> getFetch(Table table, FetchParameter... parameters) throws MappingSystemException {
        return mapper.getFetch(table, parameters);
    }

    @Override
    public <R extends AbstractRow> R add(R row) throws MappingSystemException {
        return cachedRows.add(mapper.add(row));
    }

    @Override
    public <R extends AbstractRow> void change(R oldValue, R newValue) throws MappingSystemException {
        mapper.change(oldValue, newValue);
        cachedRows.change(oldValue, newValue);
    }

    @Override
    public void delete(AbstractRow row) throws MappingSystemException {
        mapper.delete(row);
        cachedRows.delete(row);
    }

    @Override
    public int size(Table table) throws MappingSystemException {
        return mapper.size(table);
    }
}
