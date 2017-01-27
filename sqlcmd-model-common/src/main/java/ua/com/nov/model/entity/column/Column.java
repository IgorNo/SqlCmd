package ua.com.nov.model.entity.column;

import java.util.List;

public class Column {
    private ColumnID pk;
    private String name;
    private int dataType;        // SQL type from java.sql.Types
    private String typeName;     // Data source dependent type name
    private Integer columnSize;
    private Integer precision;   // the number of fractional digits. Null is returned for data types where
                                 // precision is not applicable
    private int nullable = 2;    // 0 - columnNoNulls; 1 - columnNullable; 2 - columnNullableUnknown;
    private String defaultValue; //default value for the column, which should be interpreted as a string when
                                 // the value is enclosed in single quotes
    private String remarks;       // comment describing column
    private int ordinalPosition; // index of column in table (starting at 1)
    private boolean isAutoIncrement; // Indicates whether this column is auto incremented
    private boolean isGeneratedColumn; // Indicates whether this is a generated column

    /*
     * The columnSize attribute specifies the column size for the given column.
     * For numeric data, this is the maximum precision.
     * For character data, this is the length in characters.
     * For datetime datatypes, this is the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component).
     * For binary data, this is the length in bytes.
     * For the ROWID datatype, this is the length in bytes.
     * Null is returned for data types where the column size is not applicable.
    */
    public Column(ColumnID pk, int ordinalPosition) {
        this.pk = pk;
        this.ordinalPosition = ordinalPosition;
        this.name = pk.getName();
    }

    public ColumnID getPk() {
            return pk;
        }

    public String getName() {
        return name;
    }

    public String getTypeName() {
            return typeName;
        }

    public Integer getColumnSize() {
            return columnSize;
        }

    public Integer getPrecision() {
            return precision;
        }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public int isNullable() {
        return nullable;
    }

    public void setName(String name) {
        pk.setName(name);
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public int getNullable() {
        return nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public boolean isGeneratedColumn() {
        return isGeneratedColumn;
    }

    public void setGeneratedColumn(boolean generatedColumn) {
        isGeneratedColumn = generatedColumn;
    }
}
