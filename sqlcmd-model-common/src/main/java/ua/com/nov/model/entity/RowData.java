package ua.com.nov.model.entity;

public class RowData {
    private Table table;
    private Object[] values;

    public RowData(Table table, Object[] values) {
        this.table = table;
        this.values = new Object[table.getNumberColumns()];
    }

    public Object getColumnValue(String columnName) {
        return values[table.getColumnIndex(columnName)];
    }
}
