package ua.com.nov.model;

import ua.com.nov.model.entity.data.AbstractRow;

import java.util.Map;
import java.util.WeakHashMap;

public class CachedRows {
    private static final CachedRows instance = new CachedRows();
    private static final Map<AbstractRow.Id, AbstractRow> cachedRows = new WeakHashMap<>();

    private CachedRows() {
    }

    public static CachedRows getInstance() {
        return instance;
    }

    public AbstractRow get(AbstractRow.Id id) {
        return cachedRows.get(id);
    }

    public AbstractRow put(AbstractRow.Id id, AbstractRow row) {
        return put(id, row);
    }
}
