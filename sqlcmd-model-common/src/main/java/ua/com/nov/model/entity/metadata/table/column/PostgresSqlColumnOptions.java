package ua.com.nov.model.entity.metadata.table.column;

import ua.com.nov.model.entity.metadata.server.PostgresSqlServer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PostgresSqlColumnOptions extends ColumnOptions {
    private PostgresSqlColumnOptions(Builder builder) {
        super(builder);
    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        List<String> result = new LinkedList<>();

        if (!isNotNull()) result.add("DROP NOT NULL");
        if (getDefaultValue() == null) result.add("DROP DEFAULT");
        if (!isAutoIncrement()) result.add("DROP DEFAULT");

        for (Map.Entry<String, String> entry : getOptionsMap().entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append("SET ").append(entry.getKey().trim());
            if (!entry.getValue().isEmpty())
                sb.append(' ').append(entry.getValue());
            result.add(sb.toString());
        }

        return result;
    }

    public static class Builder extends ColumnOptions.Builder<PostgresSqlColumnOptions> {

        public Builder() {
            super(PostgresSqlServer.class);
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
