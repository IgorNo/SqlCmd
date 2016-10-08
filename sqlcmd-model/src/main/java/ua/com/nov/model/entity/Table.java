package ua.com.nov.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private DataBase db;
    private String tableName;
    private String schemaName;
    private List<Column> columns = new ArrayList<Column>();
    private int indexPrimaryKey; // index of primary key in 'columns'
    private List<RowData> rows = new ArrayList<RowData>();


    public Table(DataBase db, String schemaName, String tableName) {
        this.db = db;
        this.schemaName = schemaName;
        this.tableName = tableName;
    }

    public DataBase getDb() {
        return db;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public boolean addColumn(Column column) {
        return columns.add(column);
    }

    public int getNumberColumns() {
        return columns.size();
    }
    
    public String getColumnName(int index) {
        return columns.get(index).getColumnName();
    }

    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getColumnName().equals(columnName))
                return i;
        }
        throw new IllegalArgumentException(String.format("Column %s doesn't exist in table %s", columnName, tableName));
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
