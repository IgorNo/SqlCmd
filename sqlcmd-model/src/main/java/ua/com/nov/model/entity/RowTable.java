package ua.com.nov.model.entity;

import java.util.Map;

public class RowTable {
    private DbTable table;
    private Object[] values;

    public RowTable(DbTable table, Object[] values) {
        this.table = table;
        this.values = values;
    }

    public String getColumnName(int index) {
        return table.getColumnName(index);
    }

    public Object getColumnValue(int index) {
        return values[index];
    }

}
