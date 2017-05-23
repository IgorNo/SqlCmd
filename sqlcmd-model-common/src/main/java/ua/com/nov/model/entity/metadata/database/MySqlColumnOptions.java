package ua.com.nov.model.entity.metadata.database;

public class MySqlColumnOptions extends ColumnOptions {

    public MySqlColumnOptions(Builder builder) {
        super(builder);
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
        public Builder defaultValue(String expression) {
            super.defaultValue(expression);
            return this;
        }

        public Builder generatedExpression(String expression, GeneratedColumnType type) {
            super.generatedExpression(expression);
            addOption(type.toString(), null);
            return this;
        }


        @Override
        public ColumnOptions.Builder notNull(int notNull) {
            super.notNull(notNull);
            return this;
        }

        public Builder charSet(String charSet) {
            addOption("CHARACTER SET", charSet);
            return this;
        }

        public Builder collation(String collation) {
            addOption("COLLATE", collation);
            return this;
        }

        public Builder unsigned(Boolean unsigned) {
            addOption("UNSIGNED", unsigned.toString());
            return this;
        }

        public Builder zeroFill(Boolean zeroFill) {
            addOption("ZEROFILL", zeroFill.toString());
            return this;
        }

        public Builder binari(Boolean binary) {
            addOption("BINARY", binary.toString());
            return this;
        }

        @Override
        public MySqlColumnOptions build() {
            return new MySqlColumnOptions(this);
        }
    }

    public enum GeneratedColumnType {
        STORAGE, VIRTUAL
    }
}
