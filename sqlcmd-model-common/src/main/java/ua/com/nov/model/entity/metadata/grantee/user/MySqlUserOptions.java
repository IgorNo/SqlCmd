package ua.com.nov.model.entity.metadata.grantee.user;

import ua.com.nov.model.entity.metadata.server.MysqlServer;
import ua.com.nov.model.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

public class MySqlUserOptions extends UserOptions {
    private final Map<String, String> tlsOptions;
    private final Map<String, String> resourceOption;
    private final String host;


    public MySqlUserOptions(Builder builder) {
        super(builder);
        tlsOptions = builder.tlsOptions;
        resourceOption = builder.resourceOption;
        host = builder.host;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String getCreateOptionsDefinition() {
        StringBuilder sb = new StringBuilder();
        if (getPassword() != null)
            sb.append(" IDENTIFIED BY ").append("'").append(getPassword()).append("'");
        if (tlsOptions.size() > 0)
            sb.append("\nREQUIRE  ")
                    .append(CollectionUtils.toString(tlsOptions, " AND ", " "));
        if (resourceOption.size() > 0)
            sb.append("\nWITH  ").append(CollectionUtils.toString(resourceOption, " ", " "));
        sb.append('\n').append(CollectionUtils.toString(getOptionsMap(), "\n", " "));
        return sb.toString();
    }


    public enum TlsOption {
        SSL, X509, CIPHER, ISSUER, SUBJECT
    }

    public enum ResourceOption {
        MAX_QUERIES_PER_HOUR, MAX_UPDATES_PER_HOUR, MAX_CONNECTIONS_PER_HOUR, MAX_USER_CONNECTIONS
    }

    public enum LockOption {
        LOCK, UNLOCK
    }

    public static class Builder extends UserOptions.Builder {
        private Map<String, String> tlsOptions = new HashMap<>();
        private Map<String, String> resourceOption = new HashMap<>();
        private String host = "%";

        public Builder() {
            super(MysqlServer.class);
        }

        @Override
        public Builder password(String password) {
            super.password(password);
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder addTlsOption(TlsOption name, String value) {
            tlsOptions.put(name.toString(), value.isEmpty() ? value : "'" + value + "'");
            return this;
        }

        public Builder addResourceOption(ResourceOption name, Integer value) {
            resourceOption.put(name.toString(), value.toString());
            return this;
        }

        public Builder passwordOption(String value) {
            addOption("PASSWORD EXPIRE", value);
            return this;
        }

        public Builder lockOption(LockOption option) {
            addOption("ACCOUNT", option.toString());
            return this;
        }

        @Override
        public MySqlUserOptions build() {
            return new MySqlUserOptions(this);
        }
    }
}
