package ua.com.nov.model.entity;

public class Column {
    private String columnName;
    private String columnTypeName;
    private int scale;
    private int precision;
    private boolean isAutoIncrement;
    private int isNullable = 2; // 0 - columnNoNulls; 1 - columnNullable; 2 - columnNullableUnknown;

    public Column(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
            return columnName;
        }

    public String getColumnTypeName() {
            return columnTypeName;
        }

    public int getScale() {
            return scale;
        }

    public int getPrecision() {
            return precision;
        }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public int isNullable() {
        return isNullable;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    public void setIsNullable(int isNullable) {
        this.isNullable = isNullable;
    }
}
