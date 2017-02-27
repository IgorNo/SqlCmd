package ua.com.nov.model.entity.row;

import ua.com.nov.model.entity.table.Table;

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
