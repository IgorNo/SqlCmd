package ua.com.nov.model.dao.service;

import ua.com.nov.model.dao.TableRowMapper;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.GenericTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryMapper<R extends AbstractRow<R>> implements TableRowMapper<R> {

    private static final Map<GenericTable<?>, Map<AbstractRow.Id<?>, AbstractRow<?>>> STORAGE = new ConcurrentHashMap<>();

    private GenericTable<R> table;

    @Override
    public GenericTable<R> getTable() {
        return table;
    }

    public void setTable(GenericTable<R> table) {
        this.table = table;
    }

    private AbstractRow addRowToCache(R row) {
        if (!STORAGE.containsKey(row.getTable())) {
            STORAGE.putIfAbsent(row.getTable(), new ConcurrentHashMap<>());
        }
        return STORAGE.get(row.getTable()).putIfAbsent(row.getId(), row);
    }

    private boolean removeRowFromCache(R row) {
        if (!STORAGE.containsKey(row.getTable())) return false;
        return STORAGE.get(row.getTable()).remove(row.getId(), row);
    }

    @Override
    public R get(AbstractRow.Id<R> id) throws MappingSystemException {
        if (!STORAGE.containsKey(id.getTable())) return null;
        return (R) STORAGE.get(id.getTable()).get(id);
    }

    @Override
    public List<R> getAll() throws MappingSystemException {
        List<R> result = new ArrayList<>();
        Map<AbstractRow.Id<?>, AbstractRow<?>> rowMap = STORAGE.get(table);
        if (rowMap != null) {
            for (AbstractRow row : rowMap.values()) {
                result.add((R) row);
            }
        }
        return result;
    }

    @Override
    public List<R> getN(int nStart, int number) throws MappingSystemException {
        List<R> result = new ArrayList<>();
        Map<AbstractRow.Id<?>, AbstractRow<?>> rowMap = STORAGE.get(table);
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
    public List<R> getFetch(FetchParameter... parameters) throws MappingSystemException {
        throw new UnsupportedOperationException();
    }

    @Override
    public R add(R row) throws MappingSystemException {
        if (!STORAGE.containsKey(row.getTable())) {
            STORAGE.putIfAbsent(row.getTable(), new ConcurrentHashMap<>());
        }
        return (R) STORAGE.get(row.getTable()).putIfAbsent(row.getId(), row);
    }

    @Override
    public void change(R oldValue, R newValue) throws MappingSystemException {
        STORAGE.get(newValue.getTable()).replace(oldValue.getId(), oldValue, newValue);
    }

    @Override
    public void delete(R row) throws MappingSystemException {
        STORAGE.remove(row.getId(), row);
    }

    @Override
    public int size() throws MappingSystemException {
        Map<AbstractRow.Id<?>, AbstractRow<?>> rowMap = STORAGE.get(table);
        if (rowMap != null) return rowMap.size();
        return 0;
    }
}
