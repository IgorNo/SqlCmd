package ua.com.nov.model.entity;

public class ColumnPK {
    private TablePK tablePK; // identificator of column's table
    private String name;     // column name

    public ColumnPK(TablePK tablePK, String name) {
        this.tablePK = tablePK;
        this.name = name;
    }

    public TablePK getTablePK() {
        return tablePK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
