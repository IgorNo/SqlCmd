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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColumnId)) return false;

        ColumnId columnId = (ColumnId) o;

        if (!table.equals(columnId.table)) return false;
        return name.equalsIgnoreCase(columnId.name);
    }

    @Override
    public int hashCode() {
        int result = table.hashCode();
        result = 31 * result + name.toLowerCase().hashCode();
        return result;
    }
}
