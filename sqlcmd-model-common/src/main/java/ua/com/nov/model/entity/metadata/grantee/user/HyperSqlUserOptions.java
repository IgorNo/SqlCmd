package ua.com.nov.model.entity.metadata.grantee.user;

import ua.com.nov.model.entity.metadata.server.HyperSqlServer;
import ua.com.nov.model.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class HyperSqlUserOptions extends UserOptions {

    private HyperSqlUserOptions(Builder builder) {
        super(builder);
    }

    public boolean isAdmin() {
        return getOption("ADMIN") != null;
    }

    public boolean isAuthenticationLocal() {
        return getOption("LOCAL") != null;
    }

    public String getInitialSchema() {
        return getOption("INITIAL SCHEMA");
    }

    @Override
    public String getCreateOptionsDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append(" PASSWORD ").append('"');
        if (getPassword() != null)
            sb.append(getPassword());
        sb.append('"');
        if (getOptionsMap().get("ADMIN") != null) sb.append(" ADMIN").append(';');
        return sb.toString();
    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        List<String> result = new ArrayList<>(CollectionUtils.toList(getOptionsMap(), " ", "SET"));
        StringBuilder sb = new StringBuilder();
        if (getPassword() != null) {
            sb.append("SET PASSWORD ").append('"').append(getPassword()).append('"').append(' ');
        }
        if (sb.length() > 0)
            result.add(sb.toString());
        return result;
    }

    public static class Builder extends UserOptions.Builder {

        public Builder() {
            super(HyperSqlServer.class);
        }

        @Override
        public Builder password(String password) {
            super.password(password);
            return this;
        }

        public Builder admin() {
            addOption("ADMIN", "");
            return this;
        }

        public Builder initialSchema(String schema) {
            addOption("INITIAL SCHEMA", schema);
            return this;
        }

        public Builder authenticationLocal() {
            addOption("LOCAL", "");
            return this;
        }

        @Override
        public HyperSqlUserOptions build() {
            return new HyperSqlUserOptions(this);
        }
    }
}
