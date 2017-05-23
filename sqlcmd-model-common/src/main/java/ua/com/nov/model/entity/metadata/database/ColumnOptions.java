package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.table.column.Column;

import java.util.Map;

public abstract class ColumnOptions extends MetaDataOptions<Column> {
    private final boolean autoIncrement;   // Indicates whether this column is auto incremented
    private final String generatedExpression;

    protected ColumnOptions(Builder builder) {
        super(builder);
        this.autoIncrement = builder.autoIncrement;
        this.generatedExpression = builder.generatedExpression;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public String getGeneratedExpression() {
        return generatedExpression;
    }

    public boolean isGeneratedColumn() {
        return generatedExpression != null;
    }

    public String getDefaultValue() {
        return getOption("DEFAULT");
    }

    public boolean isNotNull() {
        return getOption("NOT NULL") != null;
    }

    @Override
    public String getCreateOptionsDefinition() {
        String s = "";
        StringBuilder sb = new StringBuilder();
        if (isGeneratedColumn()) {
            sb.append("AS ").append(getGeneratedExpression());
            s = " ";
        }
        for (Map.Entry<String, String> entry : getOptionsMap().entrySet()) {
            sb.append(s).append(entry.getKey());
            if (entry.getValue() != null )
                sb.append(' ').append(entry.getValue());
            s = " ";
        }
        return sb.toString();
    }


    public abstract static class Builder<T extends ColumnOptions> extends MetaDataOptions.Builder<T> {
        private boolean autoIncrement;
        private String generatedExpression;

        public Builder(Class<? extends Database> dbClass) {
            super(dbClass);
        }

        public Builder autoIncrement(boolean autoIncrement) {
            this.autoIncrement = autoIncrement;
            if (autoIncrement) addOption(getAutoIncrementDefinition(), null);
            return this;
        }

        protected abstract String getAutoIncrementDefinition();

        protected Builder generatedExpression(String expression) {
            if (expression != null && !expression.isEmpty()) {
                this.generatedExpression = expression;
                options.remove("DEFAULT");
            }
            return this;
        }

        public Builder defaultValue(String defaultExpression) {
            if (defaultExpression != null) addOption("DEFAULT", defaultExpression);
            return this;
        }

        public Builder notNull(int notNull) {
            if (notNull == 0) addOption("NOT NULL", null);
            return this;
        }
    }
}
