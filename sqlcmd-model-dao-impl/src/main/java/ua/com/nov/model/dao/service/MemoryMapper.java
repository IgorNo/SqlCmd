package ua.com.nov.model.dao.service;

import ua.com.nov.model.dao.TableRowMapper;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.dao.fetch.FetchParameter;
import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.GenericTable;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryMapper<R extends AbstractRow<R>> implements TableRowMapper<R> {

    private static Map<GenericTable<?>, Map<AbstractRow.Id<?>, AbstractRow<?>>> STORAGE = new ConcurrentHashMap<>();

    private GenericTable<R> table;

    @Override
    public GenericTable<R> getTable() {
        return table;
    }

    public void setTable(GenericTable<R> table) {
        this.table = table;
    }

    static void clear() {
        STORAGE = new ConcurrentHashMap<>();
    }

    @Override
    public List<R> getAll() throws DAOSystemException {
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
    public List<R> getN(int nStart, int number) throws DAOSystemException {
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
    public List<R> getFetch(FetchParameter... parameters) throws DAOSystemException {
        throw new UnsupportedOperationException();
    }

    @Override
    public R get(AbstractRow.Id<R> id) throws DAOSystemException {
        if (!STORAGE.containsKey(table)) return null;
        return (R) STORAGE.get(id.getTable()).get(id);
    }

    @Override
    public R add(R row) throws DAOSystemException {
        STORAGE.putIfAbsent(table, new ConcurrentHashMap<>());
        if (STORAGE.get(table).putIfAbsent(row.getId(), row) != null) {
            throw new ConcurrentModificationException(String.format("Row with id '%s' has been already exist in table '%s'.",
                    row.getId(), table.getName()));
        }
        return row;
    }

    @Override
    public void change(R oldValue, R newValue) throws DAOSystemException {
        if (!STORAGE.get(table).replace(oldValue.getId(), oldValue, newValue)) {
            throw new ConcurrentModificationException(String.format("Row '%s' has been already changed.", oldValue));
        }
    }

    @Override
    public void delete(R row) throws DAOSystemException {
        Map<AbstractRow.Id<?>, AbstractRow<?>> rowMap = STORAGE.get(table);
        if (!STORAGE.get(row.getTable()).remove(row.getId(), row)) {
            throw new ConcurrentModificationException(String.format("Row with id '%s' has been already deleted.",
                    row.getId()));
        }
    }

    @Override
    public int size() throws DAOSystemException {
        Map<AbstractRow.Id<?>, AbstractRow<?>> rowMap = STORAGE.get(table);
        if (rowMap != null) return rowMap.size();
        return 0;
    }

    @Override
    public void deleteAll() throws DAOSystemException {
        STORAGE.remove(table);
    }
}
