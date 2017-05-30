package ua.com.nov.model.entity.metadata.table.column;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.database.ColumnOptions;
import ua.com.nov.model.entity.metadata.datatype.DataType;
import ua.com.nov.model.entity.metadata.table.Index;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.constraint.Constraint;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Column extends TableMd<Column.Id> {
    private final DataType dataType;
    private final Integer size; // column size
    private final Integer precision;    // the number of fractional digits. Null is returned for data types where
    // precision is not applicable
    private int ordinalPosition; // index of column in table (starting at 1)
    private ColumnOptions options;
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

    private Column(Builder builder) {
        super(new Id(builder.getTableId(), builder.getName()), builder);
        if (builder.precision != null && builder.precision > builder.columnSize) {
            throw new IllegalArgumentException("Precision can not be greater than column size.");
        }
        this.ordinalPosition = builder.ordinalPosition;
        this.dataType = builder.dataType;
        this.size = builder.columnSize;
        this.precision = builder.precision;
        if (builder.options == null) {
            builder.options = builder.getTableId().getDb().createColumnOptions();
        }
        if (builder.viewName == null || builder.viewName.isEmpty()) {
            builder.viewName = builder.options.getComment();
        } else {
            builder.options.comment(builder.viewName);
        }
        setViewName(builder.viewName);
        builder.options.nullable(builder.nullable).defaultValue(builder.defaultValue).autoIncrement(builder.autoIncrement);
        this.options = builder.options.build();
        if (options.getOptionsMap().size() == 0 && options.getGenerationExpression() == null)
            this.options = null;
        setOptions(this.options);
    }

    @Override
    public Column.Id getId() {
        return super.getId();
    }

    public String getName() {
        return getId().getName();
    }

    public Integer getColumnSize() {
        return size;
    }

    public Integer getPrecision() {
        return precision;
    }

    public boolean isAutoIncrement() {
        return options == null ? false : options.isAutoIncrement();
    }

    public boolean isNotNull() {
        return options == null ? false : options.isNotNull();
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getDefaultValue() {
        return options == null ? null : options.getDefaultValue();
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    @Override
    public ColumnOptions getOptions() {
        return this.options;
    }

    @Override
    public String getCreateStmtDefinition(String conflictOption) {
        final StringBuilder sb = new StringBuilder(getName());
        sb.append(" ").append(getFullTypeDeclaration());
        if (options != null) sb.append(' ').append(options.getCreateOptionsDefinition());
        return sb.toString();
    }

    public String getFullTypeDeclaration() {
        StringBuilder sb = new StringBuilder(dataType.getTypeName());
        if (size != null) {
            sb.append('(').append(size);
            if (precision != null && precision > 0) sb.append(',').append(precision);
            sb.append(')');
        }
        return sb.toString();
    }

    public static class Builder extends TableMd.Builder {
        private final DataType dataType;

        private int ordinalPosition; // index of column in table (starting at 1)
        private Integer columnSize;
        private Integer precision;  // the number of fractional digits. Null is returned for data types where
        // precision is not applicable
        private int nullable = DataType.NULL; // 0 - columnNoNulls; 1 - columnNullable; 2 - columnNullableUnknown;
        private String defaultValue; //default value for the column, which should be interpreted as a string when
        // the value is enclosed in single quotes
        private String viewName;      // comment describing column
        private boolean autoIncrement;   // Indicates whether this column is auto incremented
        private ColumnOptions.Builder<? extends ColumnOptions> options;
        private Map<Class<? extends Constraint.Builder>, Constraint.Builder<? extends Constraint>> constraints = new HashMap<>();


        public Builder(Table.Id tableId, String name, DataType dataType) {
            super(name, tableId);
            this.dataType = dataType;
        }

        public Builder(Id id, DataType dataType) {
            this(id.getTableId(), id.getName(), dataType);
        }

        public Builder(String name, DataType dataType) {
            this(null, name, dataType);
        }

        public Builder(Column col) {
            this(col.getTableId(), col.getName(), col.dataType);
            this.columnSize = col.size;
            this.precision = col.precision;
            this.viewName = col.getViewName();
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

        public Builder notNull() {
            nullable(DataType.NOT_NULL);
            return this;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder viewName(String viewName) {
            this.viewName = viewName;
            return this;
        }

        public Builder autoIncrement(boolean autoIncrement) {
            if (autoIncrement && !dataType.isAutoIncrement()) {
                throw new IllegalArgumentException("This column type can not be autoincrement.");
            }
            this.autoIncrement = autoIncrement;
            return this;
        }

        public Builder autoIncrement() {
            return autoIncrement(true);
        }

        public Builder options(ColumnOptions.Builder<? extends ColumnOptions> options) {
            this.options = options;
            return this;
        }

        public Builder ordinalPosition(int ordinalPosition) {
            this.ordinalPosition = ordinalPosition;
            return this;
        }

        public Builder addConstraint(Constraint.Builder<? extends Constraint> constraint) {
            constraints.put(constraint.getClass(), constraint);
            return this;
        }

        public Collection<Constraint.Builder<? extends Constraint>> getConstraints() {
            return constraints.values();
        }

        public Builder primaryKey(MetaDataOptions<Index> indexOptions) {
            addConstraint(new PrimaryKey.Builder(getName()).options(indexOptions));
            return this;
        }

        public Builder primaryKey() {
            addConstraint(new PrimaryKey.Builder(getName()));
            return this;
        }

        public Builder unique(MetaDataOptions<Index> indexOptions) {
            addConstraint(new UniqueKey.Builder(getName()).options(indexOptions));
            return this;
        }

        public Builder unique() {
            addConstraint(new UniqueKey.Builder(getName()));
            return this;
        }

        public Builder references(Column.Id pkColumn, ForeignKey.Rule deleteRule, ForeignKey.Rule updareRule) {
            addConstraint(new ForeignKey.Builder(getName(), pkColumn).deleteRule(deleteRule).updateRule(updareRule));
            return this;
        }

        public Builder references(Column.Id pkColumn) {
            references(pkColumn, null, null);
            return this;
        }

        public boolean isPrimaryKey() {
            return constraints.containsKey(PrimaryKey.Builder.class);
        }

        public boolean isUnique() {
            return constraints.containsKey(UniqueKey.Builder.class);
        }

        @Override
        public String generateName(String postfix) {
            return "newcol";
        }

        public Column build() {
            if (getName() == null) setName(generateName(""));
            return new Column(this);
        }
    }

    public static class Id extends TableMd.Id {
        public Id(Table.Id containerId, String name) {
            super(containerId, name);
        }

        @Override
        public String getMdName() {
            return "COLUMN";
        }
    }

}
