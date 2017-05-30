package ua.com.nov.model.entity.metadata.database;

public class MySqlColumnOptions extends ColumnOptions {

    public MySqlColumnOptions(Builder builder) {
        super(builder);
    }

    public enum GenerationColumnType {
        STORAGE, VIRTUAL
    }

    public static class Builder extends ColumnOptions.Builder<MySqlColumnOptions> {

        public Builder() {
            super(MySqlDb.class);
        }

        @Override
        protected String getAutoIncrementDefinition() {
            return "AUTO_INCREMENT";
        }

        @Override
        public Builder autoIncrement(boolean autoIncrement) {
            super.autoIncrement(autoIncrement);
            return this;
        }

        @Override
        public Builder autoIncrement() {
            super.autoIncrement();
            return this;
        }

        @Override
        public Builder defaultValue(String expression) {
            super.defaultValue(expression);
            return this;
        }

        @Override
        public Builder comment(String comment) {
            if (comment != null && !comment.isEmpty())
                addOption("COMMENT", "'" + comment + "'");
            return this;
        }

        public Builder generationExpression(String expression, GenerationColumnType type) {
            super.generationExpression(expression);
            addOption(type.toString(), "");
            return this;
        }


        @Override
        public ColumnOptions.Builder nullable(int notNull) {
            super.nullable(notNull);
            return this;
        }

        public Builder charSet(String charSet) {
            if (charSet != null && !charSet.isEmpty())
                addOption("CHARACTER SET", charSet);
            return this;
        }

        public Builder collation(String collation) {
            if (collation != null && !collation.isEmpty())
                addOption("collate", collation);
            return this;
        }

        public Builder unsigned() {
            addOption(" unsigned", "");
            return this;
        }

        public Builder zeroFill() {
            addOption(" zerofill", "");
            return this;
        }

        public Builder binari() {
            addOption(" BINARY", "");
            return this;
        }

        @Override
        public MySqlColumnOptions build() {
            return new MySqlColumnOptions(this);
        }
    }
}
