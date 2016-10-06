package ua.com.nov.model.entity;

import java.util.List;

public class DbTable {
    private DataBase db;
    private String tableName;
    private String schemaName;
    private List<Column> columns;
    private int indexPrimaryKey; // index of primary key in 'columns'

    private static class Column {
        private final String colName;
        private final String colTypeName;
        private final int colPresicion;
        private final int colScale;
        private boolean isAutoIncrement;
        private int isNullable; // 1 - column No Nulls

        public Column(String colName, String colTypeName, int colPresicion, int colScale, boolean isAutoIncrement, int isNullable) {
            this.colName = colName;
            this.colTypeName = colTypeName;
            this.colPresicion = colPresicion;
            this.colScale = colScale;
            this.isAutoIncrement = isAutoIncrement;
            this.isNullable = isNullable;
        }

        public String getColName() {
            return colName;
        }

        public String getColTypeName() {
            return colTypeName;
        }

        public int getColScale() {
            return colScale;
        }

        public int getColPresicion() {
            return colPresicion;
        }
    }

    public DbTable(DataBase db, String schemaName, String tableName) {
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
        return columns.get(index).getColName();
    }

    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).colName.equals(columnName))
                return i;
        }
        throw new IllegalArgumentException(String.format("Column %s doesn't exist in table %s", columnName, tableName));
    }
}
