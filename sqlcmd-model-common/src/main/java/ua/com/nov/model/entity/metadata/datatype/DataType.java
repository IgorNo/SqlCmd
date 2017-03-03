package ua.com.nov.model.entity.metadata.datatype;

// Builder Pattern
public class DataType {
    public static final int TYPE_NO_NULLS = 0; // does not allow NULL values
    public static final int TYPE_NULLABLE = 1; // allows NULL values
    public static final int TYPE_NULLABLE_UNKNOWN = 2;  // nullability unknown

    public static final int TYPE_PRED_NONE = 0;     // No support
    public static final int TYPE_PRED_CHAR = 1;     // Only supported with WHERE .. LIKE
    public static final int TYPE_PRED_BASIC = 2;    // Supported except for WHERE .. LIKE
    public static final int TYPE_SEARCHABLE = 3;     // Supported for all WHERE ..

    private final String typeName;
    private final int jdbcDataType; // SQL data type from java.sql.Types
    private final int precision;    // maximum precision
    private final String literalPrefix;   // prefix used to quote a literal (may be 'null')
    private final String literalSuffix;   // suffix used to quote a literal (may be 'null')
    private final String createParams;    // parameters used in creating the type (may be 'null')
    private final int nullable;     // can you use NULL for this type.
                                    // May be TYPE_NO_NULLS, TYPE_NULLABLE, TYPE_NULLABLE_UNKNOWN
    private final boolean caseSensitive;    // is it case sensitive.
    private final int searchable;           // can you use "WHERE" based on this type:
                                            // TYPE_PRED_NONE, TYPE_PRED_CHAR, TYPE_PRED_BASIC, TYPE_SEARCHABLE
    private final boolean unsignedAttribute;
    private final boolean fixedPrecScale; // can it be a money value.
    private final boolean autoIncrement;  // can it be used for an auto-increment value.
    private final String localTypeName;   // localized version of type name (may be 'null')
    private final int minimumScale;   // minimum scale supported
    private final int maximumScale;   // maximum scale supported
    private final int numPrecRadix;   // usually 2 or 10

    /* The PRECISION column represents the maximum column size that the server supports for the given datatype.
     * For numeric data, this is the maximum precision.  For character data, this is the length in characters.
     * For datetime datatypes, this is the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes.  For the ROWID datatype,
     * this is the length in bytes. Null is returned for data types where the
     * column size is not applicable.
    */

    public static class Builder {
        private final String typeName;
        private final int dataType;

        private int precision = 0;
        private String literalPrefix = null;
        private String literalSuffix = null;
        private String createParams = null;
        private int nullable = TYPE_NULLABLE_UNKNOWN;
        private boolean caseSensitive = false;
        private int searchable = TYPE_PRED_NONE;
        private boolean unsignedAttribute = false;
        private boolean fixedPrecScale = false;
        private boolean autoIncrement = false;
        private String localTypeName = null;
        private int minimumScale = 0;
        private int maximumScale = 0;
        private int numPrecRadix = 0;

        public Builder(String typeName, int dataType) {
            this.typeName = typeName;
            this.dataType = dataType;
        }

        public Builder precision(int precision) {
            this.precision = precision;
            return this;
        }

        public Builder literalPrefix(String literalPrefix) {
            this.literalPrefix = literalPrefix;
            return this;
        }

        public Builder literalSuffix(String literalSuffix) {
            this.literalSuffix = literalSuffix;
            return this;
        }

        public Builder createParams(String createParams) {
            this.createParams = createParams;
            return this;
        }

        public Builder nullable(int nullable) {
            this.nullable = nullable;
            return this;
        }

        public Builder caseSensitive(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
            return this;
        }

        public Builder searchable(int searchable) {
            this.searchable = searchable;
            return this;
        }

        public Builder unsignedAttribute(boolean unsignedAttribute) {
            this.unsignedAttribute = unsignedAttribute;
            return this;
        }

        public Builder fixedPrecScale(boolean fixedPrecScale) {
            this.fixedPrecScale = fixedPrecScale;
            return this;
        }

        public Builder autoIncrement(boolean autoIncrement) {
            this.autoIncrement = autoIncrement;
            return this;
        }

        public Builder localTypeName(String localTypeName) {
            this.localTypeName = localTypeName;
            return this;
        }

        public Builder minimumScale(int minimumScale) {
            this.minimumScale = minimumScale;
            return this;
        }

        public Builder maximumScale(int maximumScale) {
            this.maximumScale = maximumScale;
            return this;
        }

        public Builder numPrecRadix(int numPrecRadix) {
            this.numPrecRadix = numPrecRadix;
            return this;
        }

        public DataType build() {
            return new DataType(this);
        }
    }

    private DataType(Builder builder) {
        this.typeName = builder.typeName;
        this.jdbcDataType = builder.dataType;
        this.precision = builder.precision;
        this.literalPrefix = builder.literalPrefix;
        this.literalSuffix = builder.literalSuffix;
        this.createParams = builder.createParams;
        this.nullable = builder.nullable;
        this.caseSensitive = builder.caseSensitive;
        this.searchable = builder.searchable;
        this.unsignedAttribute = builder.unsignedAttribute;
        this.fixedPrecScale = builder.fixedPrecScale;
        this.autoIncrement = builder.autoIncrement;
        this.localTypeName = builder.localTypeName;
        this.minimumScale = builder.minimumScale;
        this.maximumScale = builder.maximumScale;
        this.numPrecRadix = builder.numPrecRadix;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getJdbcDataType() {
        return jdbcDataType;
    }

    public int getPrecision() {
        return precision;
    }

    public String getLiteralPrefix() {
        return literalPrefix;
    }

    public String getLiteralSuffix() {
        return literalSuffix;
    }

    public String getCreateParams() {
        return createParams;
    }

    public int getNullable() {
        return nullable;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public int getSearchable() {
        return searchable;
    }

    public boolean isUnsignedAttribute() {
        return unsignedAttribute;
    }

    public boolean isFixedPrecScale() {
        return fixedPrecScale;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public String getLocalTypeName() {
        return localTypeName;
    }

    public int getMinimumScale() {
        return minimumScale;
    }

    public int getMaximumScale() {
        return maximumScale;
    }

    public int getNumPrecRadix() {
        return numPrecRadix;
    }
}
