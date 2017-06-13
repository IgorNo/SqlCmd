package ua.com.nov.model.entity.metadata.grantee.user;

import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.server.PostgreSqlServer;
import ua.com.nov.model.util.CollectionUtils;

import java.sql.Date;

public class PostgreSqlUserOptions extends UserOptions {
    private final Encryption encryption;

    public PostgreSqlUserOptions(Builder builder) {
        super(builder);
        encryption = builder.encryption;
    }

    @Override
    public String getCreateOptionsDefinition() {
        StringBuilder sb = new StringBuilder();
        if (getPassword() != null) {
            if (encryption != null) sb.append(encryption.toString()).append(' ');
            sb.append("PASSWORD  ").append(getPassword()).append(' ');
        }
        sb.append(CollectionUtils.toString(getOptionsMap(), "\n", " "));
        return sb.toString();
    }

    public enum Encryption {
        ENCRYPTED, UNENCRYPTED
    }

    public static class Builder extends UserOptions.Builder {
        private Encryption encryption;

        public Builder() {
            super(PostgreSqlServer.class);
        }

        public Builder(PostgreSqlUserOptions that) {
            super(that);
            this.encryption = that.encryption;
        }

        @Override
        public Builder password(String password) {
            super.password(password);
            return this;
        }

        public Builder password(Encryption encryption, String password) {
            super.password(password);
            this.encryption = encryption;
            return this;
        }

        public Builder superUser() {
            addOption("SUPERUSER", "");
            return this;
        }

        public Builder noSuperUser() {
            addOption("NOSUPERUSER", "");
            return this;
        }

        public Builder createDb() {
            addOption("CREATEDB", "");
            return this;
        }

        public Builder noCreateDb() {
            addOption("NOCREATEDB", "");
            return this;
        }

        public Builder createRole() {
            addOption("CREATEROLE", "");
            return this;
        }

        public Builder noCreateRole() {
            addOption("NOCREATEROLE", "");
            return this;
        }

        public Builder inherit() {
            addOption("INHERIT", "");
            return this;
        }

        public Builder noInherit() {
            addOption("NOINHERIT", "");
            return this;
        }

        public Builder login() {
            addOption("LOGIN", "");
            return this;
        }

        public Builder noLogin() {
            addOption("NOLOGIN", "");
            return this;
        }

        public Builder replication() {
            addOption("REPLICATION", "");
            return this;
        }

        public Builder noReplication() {
            addOption("NOREPLICATION", "");
            return this;
        }

        public Builder bypassRLS() {
            addOption("BYPASSRLS", "");
            return this;
        }

        public Builder NoBypassRLS() {
            addOption("NOBYPASSRLS", "");
            return this;
        }

        public Builder connectionLimit(Integer value) {
            addOption("CONNECTION LIMIT", value.toString());
            return this;
        }

        public Builder validUntil(Date date) {
            addOption("VALID UNTIL", "'" + date.toString() + "'");
            return this;
        }

        public Builder addRole(Grantee grantee) {
            if (options.get("ROLE") == null)
                addOption("ROLE", grantee.getName());
            else
                addOption("ROLE", options.get("ROLE") + ", " + grantee.getName());
            return this;
        }

        public Builder addAdmin(Grantee grantee) {
            if (options.get("ADMIN") == null)
                addOption("ADMIN", grantee.getName());
            else
                addOption("ADMIN", options.get("ROLE") + ", " + grantee.getName());
            return this;
        }

        @Override
        public PostgreSqlUserOptions build() {
            return new PostgreSqlUserOptions(this);
        }
    }
}
