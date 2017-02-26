package ua.com.nov.model.entity.row;

import ua.com.nov.model.entity.table.TableMetaData;

public class RowData {
    private TableMetaData table;
    private Object[] values;

    public RowData(TableMetaData table, Object[] values) {
        this.table = table;
        this.values = new Object[table.getNumberOfColumns()];
    }

    public Object getColumnValue(String columnName) {
        return values[table.getColumnIndex(columnName)];
    }
}
