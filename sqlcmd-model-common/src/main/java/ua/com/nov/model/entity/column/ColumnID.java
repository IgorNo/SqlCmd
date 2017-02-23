package ua.com.nov.model.entity.column;

import ua.com.nov.model.entity.table.Table;

public class ColumnId {
    private Table table;
    private String name;

    public ColumnId(Table table, String name) {
        this.table = table;
        this.name = name;
    }

    public Table getTable() {
        return table;
    }

    public String getName() {
        return name;
    }
    
}
