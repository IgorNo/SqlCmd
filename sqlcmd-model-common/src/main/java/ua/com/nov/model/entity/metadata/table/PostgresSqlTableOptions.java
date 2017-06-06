package ua.com.nov.model.entity.metadata.table;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.server.PostgresSqlServer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PostgresSqlTableOptions extends MetaDataOptions<Table> {

    public PostgresSqlTableOptions(Builder builder) {
        super(builder);
    }

    @Override
    public String getCreateOptionsDefinition() {
        final StringBuilder sb = new StringBuilder();
        String s = "\nWITH (";
        for (Map.Entry<String, String> entry : getStorageParameters().entrySet()) {
            sb.append(s);
            sb.append("\n\t").append(entry.getKey()).append(" = ").append(entry.getValue());
            s = ",";
        }
        if (isOids() != null) sb.append(s).append("\n\t").append("OIDS = ").append(isOids());
        if (sb.length() > 0) sb.append("\n)");

        if (getOnCommit() != null) sb.append("\nON COMMIT ").append(getOnCommit());
        if (getTableSpace() != null) sb.append("\nTABLESPACE ").append(getTableSpace());

        return sb.toString();
    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        final StringBuilder sb = new StringBuilder();

        String s = "\nSET (";
        for (Map.Entry<String, String> entry : getStorageParameters().entrySet()) {
            sb.append(s);
            sb.append("\n\t").append(entry.getKey()).append(" = ").append(entry.getValue());
            s = ",";
        }
        if (sb.length() > 0) sb.append("\n)");

        if (isOids() != null) {
            if (sb.length() > 0) sb.append(",\n");
            sb.append(" SET ");
            if (isOids())
                sb.append("WITH ");
            else
                sb.append("WITHOUT ");
            sb.append("OIDS");
        }

        if (getTableSpace() != null) {
            if (sb.length() > 0) sb.append(",\n");
            sb.append("SET TABLESPACE ").append(getTableSpace());
        }

        if (getOwner() != null) {
            if (sb.length() > 0) sb.append(",\n");
            sb.append("OWNER TO ").append(getOwner());
        }

        List<String> result = new LinkedList<>();
        result.add(sb.toString());
        return result;
    }

    public String getTableSpace() {
        return getOption("TABLESPACE");
    }

    public String getOnCommit() {
        return getOption("ON COMMIT");
    }

    public String getOwner() {
        return getOption("OWNER");
    }

    public Boolean isOids() {
        return Boolean.valueOf(getOption("OIDS"));
    }

    private Map<String, String> getStorageParameters() {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : getOptionsMap().entrySet()) {
            if (entry.getKey().matches("^[a-z|_]+")) result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static class Builder extends MetaDataOptions.Builder<PostgresSqlTableOptions> {

        public Builder() {
            super(PostgresSqlServer.class);
        }

        public Builder onCommit(String onCommit) {
            addOption("ON COMMIT", onCommit);
            return this;
        }

        public Builder addStorageParameter(String name, String value) {
            addOption(name.toLowerCase(), value);
            return this;
        }

        public Builder tableSpace(String tableSpace) {
            addOption("TABLESPACE", tableSpace);
            return this;
        }

        public Builder owner(String owner) {
            addOption("OWNER", owner);
            return this;
        }

        public Builder oids(Boolean oids) {
            addOption("OIDS", oids.toString());
            return this;
        }

        @Override
        public PostgresSqlTableOptions build() {
            return new PostgresSqlTableOptions(this);
        }
    }
}
