package ua.com.nov.model.entity.metadata.database;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.server.PostgreSqlServer;
import ua.com.nov.model.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

public class PostgreSqlDbOptions extends MetaDataOptions<Database> {

    public PostgreSqlDbOptions(Builder builder) {
        super(builder);
    }

    @Override
    public String getCreateOptionsDefinition() {
        return CollectionUtils.toString(getOptionsMap(), "\n", " = ");
    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        final StringBuilder sb = new StringBuilder("");
        if (getAllowConn() != null) sb.append("\n\tALLOW_CONNECTIONS ").append(getAllowConn());
        if (getConnLimit() != null) sb.append("\n\tCONNECTION LIMIT ").append(getConnLimit());
        if (isTemplate() != null) sb.append("\n\tIS_TEMPLATE ").append(isTemplate());

        List<String> result = new LinkedList<>();
        result.add(sb.toString());

        if (getOwner() != null) result.add("OWNER TO " + getOwner());
        if (getTableSpace() != null) result.add("SET TABLESPACE " + getTableSpace());

        return result;
    }

    public String getOwner() {
        return getOption("OWNER");
    }

    public String getTableSpace() {
        return getOption("TABLESPACE");
    }

    public Boolean getAllowConn() {
        return Boolean.valueOf(getOption("ALLOW_CONNECTIONS"));
    }

    public Integer getConnLimit() {
        return Integer.valueOf(getOption("CONNECTION LIMIT"));
    }

    public Boolean isTemplate() {
        return Boolean.valueOf(getOption("IS_TEMPLATE"));
    }

    public String getEncoding() {
        return getOption("ENCODING");
    }

    public String getLcCollate() {
        return getOption("LC_COLLATE");
    }

    public String getLcType() {
        return getOption("LC_CTYPE");
    }

    public static class Builder extends MetaDataOptions.Builder<PostgreSqlDbOptions> {

        public Builder() {
            super(PostgreSqlServer.class);
        }

        public Builder owner(String owner) {
            addOption("OWNER", owner);
            return this;
        }

        public Builder tableSpace(String tableSpace) {
            addOption("TABLESPACE", tableSpace);
            return this;
        }

        public Builder allowConn(Boolean allowConn) {
            addOption("ALLOW_CONNECTIONS", allowConn.toString());
            return this;
        }

        public Builder connLimit(Integer connLimit) {
            addOption("CONNECTION LIMIT", connLimit.toString());
            return this;
        }

        public Builder isTemplate(Boolean isTemplate) {
            addOption("IS_TEMPLATE", isTemplate.toString());
            return this;
        }

        public Builder encoding(String encoding) {
            addOption("ENCODING", "'" + encoding + "'");
            return this;
        }

        public Builder lcCollate(String lcCollate) {
            addOption("LC_COLLATE", "'" + lcCollate + "'");
            return this;
        }

        public Builder lcType(String lcType) {
            addOption("LC_CTYPE", "'" + lcType + "'");
            return this;
        }

        @Override
        public PostgreSqlDbOptions build() {
            return new PostgreSqlDbOptions(this);
        }
    }

}
