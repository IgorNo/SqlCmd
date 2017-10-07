package ua.com.nov.model.entity.metadata.table.column;

import ua.com.nov.model.entity.metadata.server.PostgresqlServer;
import ua.com.nov.model.util.CollectionUtils;

import java.util.List;

public class PostgresSqlColumnOptions extends ColumnOptions {
    private PostgresSqlColumnOptions(Builder builder) {
        super(builder);
    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        List<String> result = CollectionUtils.toList(getOptionsMap(), " ", "SET");

        if (!isNotNull()) result.add("DROP NOT NULL");
        if (getDefaultValue() == null) result.add("DROP DEFAULT");
        if (!isAutoIncrement()) result.add("DROP DEFAULT");

        return result;
    }

    public static class Builder extends ColumnOptions.Builder<PostgresSqlColumnOptions> {

        public Builder() {
            super(PostgresqlServer.class);
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
