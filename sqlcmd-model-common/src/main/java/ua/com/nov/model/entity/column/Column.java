package ua.com.nov.model.entity.column;

import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.database.DataType;
import ua.com.nov.model.entity.table.Table;

public class Column implements Unique {
    private final int ordinalPosition; // index of column in table (starting at 1)
    private final ColumnId id;
    private final DataType dataType;
    private String name; // This field uses for renaming column
    private final Integer columnSize;
    private final Integer precision;  // the number of fractional digits. Null is returned for data types where
                                // precision is not applicable
    private final int nullable;    // 0 - columnNoNulls; 1 - columnNullable; 2 - columnNullableUnknown;
    private final String defaultValue; //default value for the column, which should be interpreted as a string when
    // the value is enclosed in single quotes
    private final String remarks;       // comment describing column
    private final boolean autoIncrement; // Indicates whether this column is auto incremented
    private final boolean generatedColumn; // Indicates whether this is a generated column

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

    public static class Builder {
        private final int ordinalPosition; // index of column in table (starting at 1)
        private final ColumnId id;
        private final DataType dataType;
        private Integer columnSize;

        private Integer precision;  // the number of fractional digits. Null is returned for data types where
        // precision is not applicable
        private int nullable = DataType.TYPE_NULLABLE; // 0 - columnNoNulls; 1 - columnNullable; 2 - columnNullableUnknown;
        private String defaultValue; //default value for the column, which should be interpreted as a string when
        // the value is enclosed in single quotes
        private String remarks;      // comment describing column
        private boolean autoIncrement;   // Indicates whether this column is auto incremented
        private boolean generatedColumn; // Indicates whether this is a generated column

        public Builder(int ordinalPosition, ColumnId id, DataType dataType) {
            this.ordinalPosition = ordinalPosition;
            this.id = id;
            this.dataType = dataType;
        }

        public Builder(int ordinalPosition, Table table, String name, DataType dataType) {
            this(ordinalPosition, new ColumnId(table, name), dataType);
        }

        public Builder columnSize(Integer columnSize) {
            if (columnSize != null) {
                int maxColumnSize = dataType.getPrecision();
                if (maxColumnSize > 0 && columnSize > maxColumnSize) {
                    throw new IllegalArgumentException(String.format("Column size = %d greater than the max valid value = %d",
                            columnSize, maxColumnSize));
                }
            }
            this.columnSize = columnSize;
            return this;
        }

        public Builder precision(Integer precision) {
            this.precision = precision;
            return this;
        }

        public Builder nullable(int nullable) {
            this.nullable = nullable;
            return this;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder autoIncrement(boolean autoIncrement) {
            if (!dataType.isAutoIncrement()) throw new IllegalArgumentException("This column can not be autoincrement");
            this.autoIncrement = autoIncrement;
            return this;
        }

        public Builder generatedColumn(boolean generatedColumn) {
            this.generatedColumn = generatedColumn;
            return this;
        }

        public Column build() {
            return new Column(this);
        }
    }

    public Column(Builder builder) {
        if (builder.precision != null && builder.precision > builder.columnSize) {
            throw new IllegalArgumentException("Precision can not be greater than column size");
        }
        this.ordinalPosition = builder.ordinalPosition;
        this.id = builder.id;
        this.dataType = builder.dataType;
        this.name = id.getName();
        this.columnSize = builder.columnSize;
        this.precision = builder.precision;
        this.nullable = builder.nullable;
        this.defaultValue = builder.defaultValue;
        this.remarks = builder.remarks;
        this.autoIncrement = builder.autoIncrement;
        this.generatedColumn = builder.generatedColumn;
    }


    @Override
    public ColumnId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public Integer getPrecision() {
        return precision;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public int getNullable() {
        return nullable;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public boolean isGeneratedColumn() {
        return generatedColumn;
    }

    public void setName(String name) {
        this.name = name;
    }

}
