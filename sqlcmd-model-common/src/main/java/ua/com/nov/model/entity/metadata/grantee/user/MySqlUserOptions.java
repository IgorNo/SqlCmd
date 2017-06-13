package ua.com.nov.model.entity.metadata.grantee.user;

import ua.com.nov.model.entity.metadata.server.MySqlServer;
import ua.com.nov.model.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

public class MySqlUserOptions extends UserOptions {
    private final Map<String, String> tlsOptions;
    private final Map<String, String> resourceOption;


    public MySqlUserOptions(Builder builder) {
        super(builder);
        tlsOptions = builder.tlsOptions;
        resourceOption = builder.resourceOption;
    }

    @Override
    public String getCreateOptionsDefinition() {
        StringBuilder sb = new StringBuilder();
        if (getPassword() != null)
            sb.append("IDENTIFIED BY ").append(getPassword()).append(' ');
        sb.append("REQUIRE  ").append(CollectionUtils.toString(tlsOptions, " AND ", " "));
        sb.append("WITH  ").append(CollectionUtils.toString(resourceOption, " ", " "));
        sb.append(CollectionUtils.toString(getOptionsMap(), "\n", " "));
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

        public Builder() {
            super(MySqlServer.class);
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
