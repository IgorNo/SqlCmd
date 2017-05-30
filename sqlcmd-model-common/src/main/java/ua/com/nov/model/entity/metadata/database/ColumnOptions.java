package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.table.column.Column;

import java.util.Map;

public abstract class ColumnOptions extends MetaDataOptions<Column> {
    private final boolean autoIncrement;   // Indicates whether this column is auto incremented
    private final String generationExpression;

    protected ColumnOptions(Builder builder) {
        super(builder);
        this.autoIncrement = builder.autoIncrement;
        this.generationExpression = builder.generationExpression;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public String getGenerationExpression() {
        return generationExpression;
    }

    public boolean isGeneratedColumn() {
        return generationExpression != null;
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
            sb.append("AS ").append(getGenerationExpression());
            s = " ";
        }
        for (Map.Entry<String, String> entry : getOptionsMap().entrySet()) {
            sb.append(s).append(entry.getKey().trim());
            if (!entry.getValue().isEmpty())
                sb.append(' ').append(entry.getValue());
            s = " ";
        }
        return sb.toString();
    }


    public abstract static class Builder<T extends ColumnOptions> extends MetaDataOptions.Builder<T> {
        private boolean autoIncrement;
        private String generationExpression;

        public Builder(Class<? extends Database> dbClass) {
            super(dbClass);
        }

        public Builder autoIncrement(boolean autoIncrement) {
            if (autoIncrement) {
                this.autoIncrement = true;
                addOption(getAutoIncrementDefinition(), "");
            }
            return this;
        }

        public Builder comment(String comment) {
            return this;
        }

        public Builder autoIncrement() {
           return autoIncrement(true);
        }

        protected abstract String getAutoIncrementDefinition();

        protected Builder generationExpression(String expression) {
            if (expression != null && !expression.isEmpty()) {
                this.generationExpression = "(" + expression + ")";
                options.remove("DEFAULT");
            }
            return this;
        }

        public Builder defaultValue(String defaultExpression) {
            if (defaultExpression != null && !defaultExpression.isEmpty()) addOption("DEFAULT", defaultExpression);
            return this;
        }

        public Builder nullable(int nullable) {
            if (nullable == 0) addOption("NOT NULL", "");
            return this;
        }

        public Builder notNull() {
            return nullable(0);
        }

        public String getComment() {
            return options.get("COMMENT");
        }
    }
}
