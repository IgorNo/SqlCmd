package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.metadata.datatype.DataType;

public class Column extends TableMd {
    private int ordinalPosition; // index of column in table (starting at 1)
    private final DataType dataType;
    private final Integer columnSize;
    private final Integer precision;    // the number of fractional digits. Null is returned for data types where
                                        // precision is not applicable
    private final int nullable;    // 0 - columnNoNulls; 1 - columnNullable; 2 - columnNullableUnknown;
    private final String defaultValue; //default value for the column, which should be interpreted as a string when
    // the value is enclosed in single quotes
    private final String remarks;       // comment describing column
    private final boolean autoIncrement; // Indicates whether this column is auto incremented
    private final boolean generatedColumn; // Indicates whether this is a generated column

    /*
     * The size attribute specifies the column size for the given column.
     * For numeric data, this is the maximum precision.
     * For character data, this is the length in characters.
     * For datetime datatypes, this is the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component).
     * For binary data, this is the length in bytes.
     * For the ROWID datatype, this is the length in bytes.
     * Null is returned for data types where the column size is not applicable.
    */

    public static class Builder extends TableMd.Builder {
        private final DataType dataType;

        private int ordinalPosition ; // index of column in table (starting at 1)
        private Integer columnSize;
        private Integer precision;  // the number of fractional digits. Null is returned for data types where
                                    // precision is not applicable
        private int nullable = DataType.NULL; // 0 - columnNoNulls; 1 - columnNullable; 2 - columnNullableUnknown;
        private String defaultValue; //default value for the column, which should be interpreted as a string when
                                     // the value is enclosed in single quotes
        private String remarks;      // comment describing column
        private boolean autoIncrement;   // Indicates whether this column is auto incremented
        private boolean generatedColumn; // Indicates whether this is a generated column


        public Builder(TableId tableId, String name, DataType dataType) {
            super(tableId, name);
            this.dataType = dataType;
        }

        public Builder(String name, DataType dataType) {
            this(null, name, dataType);
        }

        public Builder(Column col) {
            this(col.getId().getTableId(), col.getName(), col.dataType);
            this.columnSize = col.columnSize;
            this.precision = col.precision;
            this.nullable = col.nullable;
            this.defaultValue = col.defaultValue;
            this.remarks = col.remarks;
            this.autoIncrement = col.autoIncrement;
            this.generatedColumn = col.generatedColumn;
            this.ordinalPosition = col.ordinalPosition;
        }

        public Builder size(Integer columnSize) {
            if (columnSize != null) {
                int maxColumnSize = dataType.getPrecision();
                if (maxColumnSize > 0 && columnSize > maxColumnSize) {
                    throw new IllegalArgumentException(
                            String.format("Column size = %d greater than the max valid value = %d",
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
            if (dataType.getNullable() == DataType.NOT_NULL && nullable == DataType.NULL) {
                throw new IllegalArgumentException("This column type can not be nullable.");
            }
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
            if (autoIncrement && !dataType.isAutoIncrement()) {
                throw new IllegalArgumentException("This column type can not be autoincrement.");
            }
            this.autoIncrement = autoIncrement;
            return this;
        }

        public Builder generatedColumn(boolean generatedColumn) {
            this.generatedColumn = generatedColumn;
            return this;
        }

        public Builder ordinalPosition(int ordinalPosition) {
            this.ordinalPosition = ordinalPosition;
            return this;
        }

        public Column build() {
            return new Column(this);
        }
    }

    // вложенный класс создатся для обеспечения уникальности ключей
    private static class ColumnId extends TableMdId {
        public ColumnId(TableId containerId, String name) {
            super(containerId, name);
        }
    }

    private Column(Builder builder) {
        super(new ColumnId(builder.getTableId(), builder.getName()));
        if (builder.precision != null && builder.precision > builder.columnSize) {
            throw new IllegalArgumentException("Precision can not be greater than column size.");
        }
        this.ordinalPosition = builder.ordinalPosition;
        this.dataType = builder.dataType;
        this.columnSize = builder.columnSize;
        this.precision = builder.precision;
        this.nullable = builder.nullable;
        this.defaultValue = builder.defaultValue;
        this.remarks = builder.remarks;
        this.autoIncrement = builder.autoIncrement;
        this.generatedColumn = builder.generatedColumn;
    }

    public String getName() {
        return getId().getName();
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getName());
        sb.append(" ");
        sb.append(getFullTypeDeclaration());
        if (nullable == 0) sb.append(" NOT NULL");
        if (defaultValue != null) sb.append(" DEFAULT '").append(defaultValue).append("'");
        if (autoIncrement) sb.append(getId().getDb().getAutoIncrementDefinition());
        return sb.toString();
    }

    public String getFullTypeDeclaration() {
        StringBuilder sb = new StringBuilder(dataType.getTypeName());
        if (columnSize != null) {
            sb.append('(').append(columnSize);
            if (precision != null && precision > 0) sb.append(',').append(precision);
            sb.append(')');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ordinalPosition;
        return result;
    }
}
