package ua.com.nov.model.entity.metadata.database;

public class PostgresSqlColumnOptions extends ColumnOptions {
    private PostgresSqlColumnOptions(Builder builder) {
        super(builder);
    }

    public static class Builder extends ColumnOptions.Builder<PostgresSqlColumnOptions> {

        public Builder() {
            super(PostgresSqlDb.class);
        }

        @Override
        public PostgresSqlColumnOptions build() {
            return new PostgresSqlColumnOptions(this);
        }

        @Override
        protected String getAutoIncrementDefinition() {
            return "";
        }
    }
}
