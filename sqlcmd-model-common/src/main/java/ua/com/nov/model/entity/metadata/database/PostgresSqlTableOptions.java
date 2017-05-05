package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.table.Table;

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
        if (getStorageParameters() != null || isOids() != null) {
            sb.append("\nWITH (");
            if (isOids() != null) sb.append("\n\tOIDS = ").append(isOids()).append(',');
            if (getStorageParameters() != null) sb.append("\n\t").append(getStorageParameters());
            sb.append("\n)");
        }
        if (getTableSpace() != null) sb.append("\nSET TABLESPACE ").append(getTableSpace());
        if (getOnCommit() != null) sb.append("\nON COMMIT ").append(getOnCommit());
        return sb.toString();
    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        final StringBuilder sb = new StringBuilder();

        if (isOids() != null) {
            sb.append("\n SET ");
            if (isOids())
                sb.append("WITH ");
            else
                sb.append("WITHOUT ");
            sb.append("OIDS");
        }

        if (getStorageParameters() != null) {
            if (sb.length() > 0) sb.append(",\n\t");
            sb.append("\n SET (");
            sb.append('\n').append(getStorageParameters());
            sb.append("\n)");
        }

        if (getTableSpace() != null) {
            if (sb.length() > 0) sb.append(",\n");
            sb.append("TABLESPACE ").append(getTableSpace());
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

    public String getStorageParameters() {
        return getOption("STORAGE PARAMETERS");
    }

    public static class Builder extends MetaDataOptions.Builder<PostgresSqlTableOptions> {
        private Map<String, String> storageParameters = new HashMap<>();

        public Builder() {
            super(PostgresSqlDb.class);
        }

        public Builder onCommit(String onCommit) {
            addOption("ON COMMIT", onCommit);
            return this;
        }

        public Builder addStorageParameter(String name, String value) {
            this.storageParameters.put(name, value);
            return this;
        }

        public Builder storageParameters(String parameters) {
            addOption("STORAGE PARAMETERS", parameters);
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
            if (storageParameters.size() > 0) {
                StringBuilder sb = new StringBuilder();
                String s = "";
                for (Map.Entry<String, String> entry : storageParameters.entrySet()) {
                    sb.append(s).append(entry.getKey()).append(" = ").append(entry.getValue());
                    s = ",\n ";
                }
                addOption("STORAGE PARAMETERS", sb.toString());
            }
            return new PostgresSqlTableOptions(this);
        }
    }
}
