package ua.com.nov.model.entity.table;

import ua.com.nov.model.entity.row.RowData;
import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.column.ColumnPK;
import ua.com.nov.model.entity.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private TablePK pk;     // table primary key
    private String name;    // table name
    private String type;    // table type.  Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
                            //                                "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
    private String remarks;  // explanatory comment on the table

    private Map<ColumnPK, Column> columns = new HashMap<>(); // table columns
    private int indexPrimaryKey;                      // index of primary key in 'columns'
    private List<RowData> rows = new ArrayList<>();   // table data

    public Table(TablePK pk) {
        this(pk, "TABLE");
    }

    public Table(Database db, String catalog, String schema, String name) {
        this(new TablePK(db, catalog, schema, name));
    }

    public Table(TablePK pk, String type) {
        this.pk = pk;
        this.name = pk.getName();
        this.type = type;
    }

    public TablePK getPk() {
        return pk;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public void clearColumns() {
        columns.clear();
    }

    public boolean addColumn(Column column) {
        if (columns.containsKey(column.getPk())) return false;
        columns.put(column.getPk(), column);
        return true;
    }

    public void addAllColums(Map<ColumnPK, Column> columnMap) {
        columns.putAll(columnMap);
    }

    public int getNumberColumns() {
        return columns.size();
    }

    public String getColumnName(int index) {
        return columns.get(index).getPk().getName();
    }

    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getPk().equals(columnName))
                return i;
        }
        throw new IllegalArgumentException(String.format("Column %s doesn't exist in table %s", columnName, pk.getName()));
    }
}
