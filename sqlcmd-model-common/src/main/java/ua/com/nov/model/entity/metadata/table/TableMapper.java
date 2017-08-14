package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.data.AbstractRow;

import java.util.HashMap;
import java.util.Map;

public class TableMapper {
    private static final Map<Table.Id, GenericTable<?>> GENERIC_TABLE_MAP = new HashMap<>();

    private TableMapper() {
    }

    public static GenericTable<?> getGenericTable(Table.Id id) {
        return GENERIC_TABLE_MAP.get(id);
    }

    public static <R extends AbstractRow<R>> GenericTable<R> getGenericTable(Table.Id id, Class<R> rowClass) {
        return (GenericTable<R>) GENERIC_TABLE_MAP.get(id);
    }

    public static <R extends AbstractRow<R>> void addTable(Table.Id id, GenericTable<R> table) {
        GENERIC_TABLE_MAP.putIfAbsent(id, table);
    }
}
