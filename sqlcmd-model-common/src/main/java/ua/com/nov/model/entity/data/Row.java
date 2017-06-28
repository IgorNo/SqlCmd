package ua.com.nov.model.entity.data;

import ua.com.nov.model.entity.Buildable;
import ua.com.nov.model.entity.Hierarchical;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.datatype.DataTypes;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;

import java.util.*;

public class Row implements Unique<Row.Id> {
    private final Table table;
    private final Object[] values;
    private final Map<Table, Row> foreignKeys;

    public Row(Builder builder) {
        table = builder.table;
        values = builder.values;
        foreignKeys = builder.foreignKeys;
    }

    public Table getTable() {
        return table;
    }

    public Object getValue(String column) {
        return values[table.getColumn(column).getOrdinalPosition()];
    }

    public Object getValue(int ordinalPosition) {
        return values[ordinalPosition];
    }


    public Row getForeignKeyValue(Table fk) {
        return foreignKeys.get(fk);
    }

    public Row getForeignKeyValue(String fk) {
        for (Map.Entry<Table, Row> entry : foreignKeys.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(fk)) return entry.getValue();
        }
        return null;
    }

    public Id getId() {
        Id id = new Id(table);
        int i = 0;
        for (String s : table.getPrimaryKey().getColumnNamesList()) {
            int position = table.getColumn(s).getOrdinalPosition();
            id.values[i++] = values[position];
        }
        return id;
    }

    public Column getValueColumn(int ordinalPosition) {
        return table.getColumn(ordinalPosition);
    }

    public int getValueSqlType(int ordinalPosition) {
        return getValueColumn(ordinalPosition).getDataType().getJdbcDataType();
    }


    public static class Id implements Hierarchical<Table> {
        private final Table table;
        private Object[] values;

        private Id(Table table) {
            this.table = table;
            this.values = new Object[table.getPrimaryKey().getNumberOfColumns()];
        }

        public Id(Table table, Object... values) {
            int length = table.getPrimaryKey().getNumberOfColumns();
            if (length == values.length) {
                this.table = table;
                this.values = new Object[length];
                for (int i = 0; i < length; i++) {
                    String columnName = table.getPrimaryKey().getColumn(i).getName();
                    Column column = table.getColumn(columnName);
                    Class<?> columnClass = DataTypes.getClazz(column.getDataType().getJdbcDataType());
                    if (values[i].getClass() == columnClass)
                        this.values[i] = values[i];
                    else
                        throw new IllegalArgumentException(String.format("Mismatch between column and value classes " +
                                        "(column class = '%s', value class = '%s') in position '%d'.",
                                columnClass, values[i].getClass(), i));
                }
            } else {
                throw new IllegalArgumentException(String.format("Mismatch between number of columns in PrimaryKey = '%d' " +
                        "and number of values in parametes = '%d'", length, values.length));
            }
        }

        @Override
        public Server getServer() {
            return table.getServer();
        }

        @Override
        public Table getContainerId() {
            return table;
        }

        public Table getTable() {
            return table;
        }

        public List<Object> getValues() {
            return new ArrayList<>(Arrays.asList(values));
        }

        public Object getValue(int ordinalPosition) {
            return values[ordinalPosition];
        }

        public Column getValueColumn(int ordinalPosition) {
            List<String> idColumns = table.getPrimaryKey().getColumnNamesList();
            return table.getColumn(idColumns.get(ordinalPosition));
        }

        public int getValueSqlType(int ordinalPosition) {
            return getValueColumn(ordinalPosition).getDataType().getJdbcDataType();
        }
    }

    public static class Builder implements Buildable<Row> {
        private final Table table;
        private final Object[] values;
        private Map<Table, Row> foreignKeys;

        public Builder(Table table) {
            this.table = table;
            this.values = new Object[table.getNumberOfColumns()];
            List<ForeignKey> foreignKeys = table.getForeignKeyList();
            if (foreignKeys.size() > 0) {
                this.foreignKeys = new HashMap<>();
            }
        }

        public Builder(Row row) {
            this(row.getTable());
            for (int i = 0; i < values.length; i++) {
                this.values[i] = row.values[i];
            }
            for (Map.Entry<Table, Row> entry : row.foreignKeys.entrySet()) {
                this.foreignKeys.put(entry.getKey(), entry.getValue());
            }
        }

        public Builder setValue(String column, Object value) {
            values[table.getColumn(column).getOrdinalPosition()] = value;
            return this;
        }

        public Builder setForeignKey(Row value) {
            ForeignKey fk = table.getForeignKey(value.table.getId());
            if (fk != null) {
                foreignKeys.put(value.table, value);
                ForeignKey key = table.getForeignKey(value.table.getId());
                for (int i = 1; i <= key.getNumberOfColumns(); i++) {
                    setValue(key.getFkColumn(i), value.getValue(key.getPkColumn(i).getName()));
                }
            } else
                throw new IllegalArgumentException(String.format("Table '%s' doesn't refer to table '%s'.",
                        table.getFullName(), value.table.getFullName()));
            return this;
        }

        @Override
        public Row build() {
            return new Row(this);
        }
    }
}
