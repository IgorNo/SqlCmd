package ua.com.nov.model.entity.data;

import org.springframework.jdbc.support.KeyHolder;
import ua.com.nov.model.dao.TableRowMapper;
import ua.com.nov.model.dao.exception.BusinessLogicException;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.entity.Buildable;
import ua.com.nov.model.entity.Persistance;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.datatype.DataTypes;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.entity.metadata.table.GenericTable;
import ua.com.nov.model.entity.metadata.table.TableMapper;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class AbstractRow<R extends AbstractRow<R>> implements Unique<AbstractRow.Id<R>> {
    protected final Object[] values;
    private final GenericTable<R> table;
    private final Map<GenericTable<?>, ProxyRow> foreignKeys;
    private AbstractRow.Id id;

    protected AbstractRow(Builder builder) {
        table = builder.table;
        values = builder.values;
        foreignKeys = builder.foreignKeys;
    }

    public GenericTable<R> getTable() {
        return table;
    }

    public <T> T getValue(String column) {
        return (T) values[table.getColumn(column).getOrdinalPosition() - 1];
    }

    public <T> T getValue(int ordinalPosition) {
        return (T) values[ordinalPosition - 1];
    }

    public <T extends AbstractRow<T>> T getForeignKeyValue(GenericTable<T> fk) throws DAOSystemException {
        ProxyRow row = foreignKeys.get(fk);
        return (T) row.getRow(id);
    }

    public <T extends AbstractRow<T>> Id<T> getForeignKeyId(ForeignKey key, Class<T> rowClass)
            throws DAOSystemException {

        int number = key.getNumberOfColumns();
        Object[] keys = new Object[number];
        for (int i = 0; i < number; i++) {
            String columnName = key.getFkColumn(i + 1);
            keys[i] = getValue(columnName);
        }
        GenericTable<T> table = TableMapper.getGenericTable(key.getTableId(), rowClass);
        Class[] paramTypes = new Class[]{GenericTable.class, Object[].class};
        try {
            Constructor<?> constructor = Class.forName(table.getClass().getName() + "$Id").getConstructor(paramTypes);
            return (Id<T>) constructor.newInstance(table, keys);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException e) {
            throw new BusinessLogicException("Create Id Error.\n", e);
        }
    }

    public AbstractRow getForeignKeyValue(String fk) throws DAOSystemException {
        for (Map.Entry<GenericTable<?>, ProxyRow> entry : foreignKeys.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(fk)) return entry.getValue().getRow(id);
        }
        return null;
    }

    public Id<R> getId() {
        return id;
    }

    protected void initId(Id<R> id) {
        int i = 0;
        for (String s : table.getPrimaryKey().getColumnNamesList()) {
            int ordinalPosition = table.getColumn(s).getOrdinalPosition();
            id.values[i++] = values[ordinalPosition - 1];
        }
        this.id = id;
    }

    public Column getValueColumn(int ordinalPosition) {
        return table.getColumn(ordinalPosition);
    }

    public int getValueSqlType(int ordinalPosition) {
        return getValueColumn(ordinalPosition).getDataType().getSqlType();
    }

    public <T extends AbstractRow<T>> void setForeignKey(TableRowMapper<T> mapper) {
        foreignKeys.put(mapper.getTable(), new ProxyRow<>(mapper));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractRow row = (AbstractRow) o;

        if (!table.equals(row.table)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(values, row.values);
    }

    @Override
    public int hashCode() {
        int result = table.hashCode();
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Table '").append(table.getFullName()).append("'");
        String s = " {";
        for (Column column : table.getColumns()) {
            sb.append(s).append("'").append(column.getName()).append("' = ")
                    .append(values[table.getColumnIndex(column.getName())]);
            s = ", ";
        }
        sb.append('}');
        return sb.toString();
    }

    public static class Id<R extends AbstractRow<R>> implements Persistance<GenericTable<R>> {
        private final GenericTable<R> table;
        protected Object[] values;

        public Id(GenericTable<R> table, Object... values) {
            int length = table.getPrimaryKey().getNumberOfColumns();
            if (values.length == 0 || length == values.length) {
                this.table = table;
                this.values = new Object[length];
                for (int i = 0; i < values.length; i++) {
                    String columnName = table.getPrimaryKey().getColumn(i + 1).getName();
                    Column column = table.getColumn(columnName);
                    Class<?> columnClass = DataTypes.getClazz(column.getDataType().getSqlType()).get(0);
                    if (values[i].getClass() == columnClass)
                        this.values[i] = values[i];
                    else
                        throw new IllegalArgumentException(String.format("Mismatch between column and value classes " +
                                        "(column class = '%s', value class = '%s') in position '%d'.",
                                columnClass, values[i].getClass(), i));
                }
            } else {
                throw new IllegalArgumentException(String.format("Mismatch between number of addColumnList in PrimaryKey = '%d' " +
                        "and number of values in parametes = '%d'", length, values.length));
            }
        }

        @Override
        public Server getServer() {
            return table.getServer();
        }

        @Override
        public GenericTable<R> getContainerId() {
            return table;
        }

        public GenericTable<R> getTable() {
            return table;
        }

        public List<Object> getValues() {
            return new ArrayList<>(Arrays.asList(values));
        }

        public Object getValue(int ordinalPosition) {
            return values[ordinalPosition - 1];
        }

        public Column getValueColumn(int ordinalPosition) {
            List<String> idColumns = table.getPrimaryKey().getColumnNamesList();
            return table.getColumn(idColumns.get(ordinalPosition - 1));
        }

        public int getValueSqlType(int ordinalPosition) {
            return getValueColumn(ordinalPosition).getDataType().getSqlType();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Id id = (Id) o;

            if (!table.equals(id.table)) return false;
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return Arrays.equals(values, id.values);
        }

        @Override
        public int hashCode() {
            int result = table.hashCode();
            result = 31 * result + Arrays.hashCode(values);
            return result;
        }

        @Override
        public String toString() {
            return Arrays.toString(values);
        }
    }

    public abstract static class Builder<R extends AbstractRow<R>> implements Buildable<R> {
        private final GenericTable<R> table;
        private final Object[] values;
        private Map<GenericTable<?>, ProxyRow> foreignKeys;

        public Builder(GenericTable<R> table) {
            this.table = table;
            this.values = new Object[table.getNumberOfColumns()];
            List<ForeignKey> foreignKeys = table.getForeignKeyList();
            if (foreignKeys.size() > 0) {
                this.foreignKeys = new LinkedHashMap<>();
            }
        }

        public Builder(AbstractRow<R> row) {
            this(row.getTable());
            assign(row);
        }

        public void assign(AbstractRow<R> row) {
            for (int i = 0; i < values.length; i++) {
                this.values[i] = row.values[i];
            }
            if (row.foreignKeys != null) {
                for (Map.Entry<GenericTable<?>, ProxyRow> entry : row.foreignKeys.entrySet()) {
                    this.foreignKeys.put(entry.getKey(), entry.getValue());
                }
            }
        }

        public Builder setId(KeyHolder id) {
            if (id.getKeys().size() == 1) {
                for (Column column : table.getColumns()) {
                    if (column.isAutoIncrement()) {
                        setValue(column.getName().toLowerCase(), id.getKey());
                        break;
                    }
                }
            } else {
                for (Map.Entry<String, Object> entry : id.getKeys().entrySet()) {
                    setValue(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        public Builder setValue(String column, Object value) {
            values[table.getColumn(column).getOrdinalPosition() - 1] = value;
            return this;
        }

        public <T extends AbstractRow<T>> Builder setForeignKey(T row) {
            ForeignKey fk = table.getForeignKey(row.getTable().getId());
            if (fk != null) {
                foreignKeys.put(row.getTable(), new ProxyRow(row));
                ForeignKey key = table.getForeignKey(row.getTable().getId());
                for (int i = 1; i <= key.getNumberOfColumns(); i++) {
                    setValue(key.getFkColumn(i), row.getValue(key.getPkColumn(i).getName()));
                }
            } else
                throw new IllegalArgumentException(String.format("Table '%s' doesn't refer to table '%s'.",
                        table.getFullName(), row.getTable().getFullName()));
            return this;
        }
    }
}
