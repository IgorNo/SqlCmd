package ua.com.nov.model.entity.column;

import ua.com.nov.model.entity.table.TableID;

public class ColumnID {
    private TableID tableID; // identificator of column's table
    private String name;     // column name

    public ColumnID(TableID tableID, String name) {
        this.tableID = tableID;
        this.name = name;
    }

    public TableID getTableID() {
        return tableID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
