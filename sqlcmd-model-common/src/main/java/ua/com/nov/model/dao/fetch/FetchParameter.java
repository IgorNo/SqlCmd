package ua.com.nov.model.dao.fetch;

import ua.com.nov.model.entity.metadata.datatype.DataTypes;
import ua.com.nov.model.entity.metadata.table.column.Column;

public abstract class FetchParameter {

    private Column column;
    private Object value;

    public FetchParameter(Column column, Object value) {
        this.column = column;
        this.value = value;
        if (!DataTypes.getClazz(column.getSqlType()).contains(value.getClass()))
            throw new IllegalArgumentException("Mismatch between column and value");
    }

    public Column getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }
}
