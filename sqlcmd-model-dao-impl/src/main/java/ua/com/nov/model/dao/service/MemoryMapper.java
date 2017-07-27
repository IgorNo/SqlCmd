package ua.com.nov.model.dao.service;

import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryMapper implements Mapper {

    private final Map<Table, Map<AbstractRow.Id, AbstractRow>> storage = new ConcurrentHashMap<>();

    private AbstractRow addRowToCache(AbstractRow row) {
        if (!storage.containsKey(row.getTable())) {
            storage.putIfAbsent(row.getTable(), new ConcurrentHashMap<>());
        }
        return storage.get(row.getTable()).putIfAbsent(row.getId(), row);
    }

    private boolean removeRowFromCache(AbstractRow row) {
        if (!storage.containsKey(row.getTable())) return false;
        return storage.get(row.getTable()).remove(row.getId(), row);
    }

    @Override
    public <R extends AbstractRow> R get(AbstractRow.Id id) throws MappingSystemException {
        if (!storage.containsKey(id.getTable())) return null;
        return (R) storage.get(id.getTable()).get(id);
    }

    @Override
    public <R extends AbstractRow> List<R> getAll(Table table) throws MappingSystemException {
        List<R> result = new ArrayList<>();
        Map<AbstractRow.Id, AbstractRow> rowMap = storage.get(table);
        if (rowMap != null) {
            for (AbstractRow row : rowMap.values()) {
                result.add((R) row);
            }
        }
        return result;
    }

    @Override
    public <R extends AbstractRow> List<R> getN(Table table, int nStart, int number) throws MappingSystemException {
        List<R> result = new ArrayList<>();
        Map<AbstractRow.Id, AbstractRow> rowMap = storage.get(table);
        if (rowMap != null) {
            int index = 1;
            for (AbstractRow row : rowMap.values()) {
                if (index >= (nStart + number) || index > rowMap.size()) return result;
                if (index >= nStart) {
                    result.add((R) row);
                }
                index++;
            }
        }
        return result;
    }

    @Override
    public <R extends AbstractRow> List<R> getFetch(Table table, FetchParameter... parameters) throws MappingSystemException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R extends AbstractRow> R add(R row) throws MappingSystemException {
        if (!storage.containsKey(row.getTable())) {
            storage.putIfAbsent(row.getTable(), new ConcurrentHashMap<>());
        }
        return (R) storage.get(row.getTable()).putIfAbsent(row.getId(), row);
    }

    @Override
    public <R extends AbstractRow> void change(R oldValue, R newValue) throws MappingSystemException {
        storage.get(newValue.getTable()).replace(oldValue.getId(), oldValue, newValue);
    }

    @Override
    public void delete(AbstractRow row) throws MappingSystemException {
        storage.remove(row.getId(), row);
    }

    @Override
    public int size(Table table) throws MappingSystemException {
        Map<AbstractRow.Id, AbstractRow> rowMap = storage.get(table);
        if (rowMap != null) return rowMap.size();
        return 0;
    }
}
