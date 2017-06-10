package ua.com.nov.model.entity.metadata.table.column;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.util.CollectionUtils;

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
        StringBuilder sb = new StringBuilder();
        if (isGeneratedColumn()) {
            sb.append("AS ").append(getGenerationExpression()).append(' ');
        }
        sb.append(CollectionUtils.toString(getOptionsMap(), " ", " "));
        return sb.toString();
    }


    public abstract static class Builder<T extends ColumnOptions> extends MetaDataOptions.Builder<T> {
        private boolean autoIncrement;
        private String generationExpression;

        public Builder(Class<? extends Server> dbClass) {
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
