package ua.com.nov.model;

import ua.com.nov.model.entity.data.AbstractRow;
import ua.com.nov.model.entity.metadata.table.Table;

import java.util.HashMap;
import java.util.Map;

public class TableMapper {
    private static final Map<Table, Class<? extends AbstractRow>> TABLE_MAPPER = new HashMap<>();

    private TableMapper() {
    }

    public static Class<? extends AbstractRow> getRowClass(Table table) {
        return TABLE_MAPPER.get(table);
    }

    public static void addRowClass(Table table, Class<? extends AbstractRow> clazz) {
        TABLE_MAPPER.put(table, clazz);
    }
}
