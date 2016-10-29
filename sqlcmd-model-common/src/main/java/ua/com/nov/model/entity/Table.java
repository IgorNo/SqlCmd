package ua.com.nov.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private TablePK pk;
    private String type;    // table type.  Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
                            //                                "LOCAL TEMPORARY", "ALIAS", "SYNONYM".

    private List<Column> columns = new ArrayList<>(); // table columns
    private int indexPrimaryKey;                      // index of primary key in 'columns'
    private List<RowData> rows = new ArrayList<>();   // table data


    public Table(TablePK pk, String type) {
        this.pk = pk;
        this.type = type;
    }

    public TablePK getPk() {
        return pk;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return pk.getName();
    }

    public boolean addColumn(Column column) {
        return columns.add(column);
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

    public class RowData {
        private Object[] values;

        public RowData(Table table, Object[] values) {
            this.values = new Object[getNumberColumns()];
        }

        public Object getColumnValue(String columnName) {
            return values[getColumnIndex(columnName)];
        }

    }
}
