package ua.com.nov.model.entity.metadata.datatype;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum DataTypes {

    BIT(Types.BIT, Boolean.class), TINYINT(Types.TINYINT, Byte.class), SMALLINT(Types.SMALLINT, Short.class),
    INTEGER(Types.INTEGER, Integer.class), BIGINT(Types.BIGINT, Long.class),

    FLOAT(Types.FLOAT, Float.class), REAL(Types.REAL, Float.class), DOUBLE(Types.DOUBLE, Double.class),
    NUMERIC(Types.NUMERIC, BigDecimal.class), DECIMAL(Types.DECIMAL, BigDecimal.class),

    CHAR(Types.CHAR, String.class), VARCHAR(Types.VARCHAR, String.class), LONGVARCHAR(Types.LONGVARCHAR, String.class),
    NCHAR(Types.NCHAR, String.class), NVARCHAR(Types.NVARCHAR, String.class),
    LONGNVARCHAR(Types.LONGNVARCHAR, String.class),

    DATE(Types.DATE, Date.class), TIME(Types.TIME, Time.class), TIMESTAMP(Types.TIMESTAMP, Timestamp.class),

    BINARY(Types.BINARY, Byte[].class), VARBINARY(Types.BINARY, Byte[].class),
    LONGVARBINARY(Types.LONGVARBINARY, Byte[].class),

    ARRAY(Types.ARRAY, Array.class),

    BLOB(Types.BLOB, Blob.class), CLOB(Types.CLOB, Clob.class), NCLOB(Types.NCLOB, NClob.class),

    ROWID(Types.ROWID, RowId.class),

    SQLXML(Types.SQLXML, java.sql.SQLXML.class),

    BOOLEAN(Types.BOOLEAN, Boolean.class),

    NULL(Types.NULL, null),

    OTHER(Types.OTHER, Object.class);

    private final int jdbcDataType;
    private final Class<?> clazz;

    DataTypes(int jdbcDataType, Class<?> clazz) {
        this.jdbcDataType = jdbcDataType;
        this.clazz = clazz;
    }

    public static List<Class<?>> getClazz(int jdbcDataType) {
        List<Class<?>> result = new ArrayList<>();
        for (DataTypes type : values()) {
            if (type.jdbcDataType == jdbcDataType) result.add(type.clazz);
        }
        if (result.size() > 0)
            return result;
        throw new IllegalArgumentException(String.format("Types %s dosn't exist.", jdbcDataType));
    }

    public static List<String> getTypeName(int jdbcDataType) {
        List<String> result = new ArrayList<>();
        for (DataTypes type : values()) {
            if (type.jdbcDataType == jdbcDataType) result.add(type.toString());
        }
        if (result.size() > 0)
            return result;
        throw new IllegalArgumentException(String.format("Types %s dosn't exist.", jdbcDataType));
    }

    public static int getJdbcDataType(Class<?> clazz) {
        for (DataTypes type : values()) {
            if (type.clazz == clazz) return type.jdbcDataType;
        }
        throw new IllegalArgumentException(String.format("The class '%s' isn't SQL data type.", clazz));
    }

    public int getJdbcDataType() {
        return jdbcDataType;
    }
}

