package ua.com.nov.model.entity.data;

import ua.com.nov.model.entity.metadata.table.Table;

public class RowData {
    private Table table;
    private Object[] values;

    public RowData(Table table, Object[] values) {
        this.table = table;
        this.values = new Object[table.getNumberOfColumns()];
    }

    public Object getColumnValue(String columnName) {
        return values[table.getColumnIndex(columnName)];
    }
}
