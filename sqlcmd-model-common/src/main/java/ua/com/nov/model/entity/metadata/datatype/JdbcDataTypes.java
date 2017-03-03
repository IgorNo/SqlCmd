package ua.com.nov.model.entity.metadata.datatype;

import java.sql.Types;

public enum JdbcDataTypes {

        BIT(Types.BIT), TINYINT(Types.TINYINT), SMALLINT(Types.SMALLINT), INTEGER(Types.INTEGER), BIGINT(Types.BIGINT),
        FLOAT(Types.FLOAT), REAL(Types.REAL), DOUBLE(Types.DOUBLE),
        NUMERIC(Types.NUMERIC), DECIMAL(Types.DECIMAL),
        CHAR(Types.CHAR), VARCHAR(Types.VARCHAR), LONGVARCHAR(Types.LONGNVARCHAR),
        DATE(Types.DATE), TIME(Types.TIME), TIMESTAMP(Types.TIMESTAMP),
        BINARY(Types.BINARY), VARBINARY(Types.BINARY), LONGVARBINARY(Types.LONGVARBINARY),
        BOOLEAN(Types.BOOLEAN),
        NULL(Types.NULL), OTHER(Types.OTHER);

        private final int jdbcDataType;

        JdbcDataTypes(int jdbcDataType) {
            this.jdbcDataType = jdbcDataType;
        }

        public int getJdbcDataType() {
            return jdbcDataType;
        }

        public String getTypeName(int sqlDataType) {
            for (JdbcDataTypes type : values()) {
                if (type.getJdbcDataType() == sqlDataType) return type.toString();
            }
            throw new IllegalArgumentException();
        }
}

