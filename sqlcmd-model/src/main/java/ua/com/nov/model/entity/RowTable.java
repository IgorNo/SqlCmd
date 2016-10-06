package ua.com.nov.model.entity;

public class RowTable {
    private DbTable table;
    private Object[] values;

    public RowTable(DbTable table, Object[] values) {
        this.table = table;
        this.values = new Object[table.getNumberColumns()];
    }

    public Object getColumnValue(String columnName) {
        return values[table.getColumnIndex(columnName)];
    }

}
