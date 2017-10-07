package ua.com.nov.model.entity.metadata.grantee.user;

import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.server.PostgresqlServer;
import ua.com.nov.model.util.CollectionUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PostgreSqlUserOptions extends UserOptions {
    private List<String> roles = new ArrayList<>();
    private List<String> admins = new ArrayList<>();


    public PostgreSqlUserOptions(Builder builder) {
        super(builder);
        roles = builder.roles;
        admins = builder.admins;
    }

    @Override
    public String getCreateOptionsDefinition() {
        StringBuilder sb = new StringBuilder(getUpdateOptionsDefinition().get(0));
        if (roles.size() > 0) sb.append("ROLE ").append(CollectionUtils.toString(roles)).append('\n');
        if (admins.size() > 0) sb.append("ADMIN ").append(CollectionUtils.toString(admins)).append('\n');
        return sb.toString();
    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        StringBuilder sb = new StringBuilder();
        if (getPassword() != null) {
            sb.append(" PASSWORD  ").append("'").append(getPassword()).append("'\n");
        }
        sb.append(' ').append(CollectionUtils.toString(getOptionsMap(), "\n", " "));
        List<String> result = new ArrayList<>();
        result.add(sb.toString());
        return result;
    }

    public static class Builder extends UserOptions.Builder {
        private List<String> roles = new ArrayList<>();
        private List<String> admins = new ArrayList<>();

        public Builder() {
            super(PostgresqlServer.class);
        }

        public Builder(PostgreSqlUserOptions that) {
            super(that);
        }

        @Override
        public Builder password(String password) {
            super.password(password);
            return this;
        }

        public Builder superUser(boolean trigger) {
            if (trigger)
                addOption("SUPERUSER", "");
            else
                addOption("NOSUPERUSER", "");
            return this;
        }

        public Builder createDb(boolean trigger) {
            if (trigger)
                addOption("CREATEDB", "");
            else
                addOption("NOCREATEDB", "");
            return this;
        }

        public Builder createRole(boolean trigger) {
            if (trigger)
            addOption("CREATEROLE", "");
            else
                addOption("NOCREATEROLE", "");
            return this;
        }

        public Builder inherit(boolean trigger) {
            if (trigger)
            addOption("INHERIT", "");
            else
                addOption("NOINHERIT", "");
            return this;
        }

        public Builder canLogin(boolean trigger) {
            if (trigger)
            addOption("LOGIN", "");
            else
                addOption("NOLOGIN", "");
            return this;
        }

        public Builder replication(boolean trigger) {
            if (trigger)
            addOption("REPLICATION", "");
            else
                addOption("NOREPLICATION", "");
            return this;
        }

        public Builder bypassRLS(boolean trigger) {
            if (trigger)
            addOption("BYPASSRLS", "");
            else
                addOption("NOBYPASSRLS", "");
            return this;
        }

        public Builder connectionLimit(Integer value) {
            addOption("CONNECTION LIMIT", value.toString());
            return this;
        }

        public Builder validUntil(Date date) {
            if (date != null)
                addOption("VALID UNTIL", "'" + date.toString() + "'");
            return this;
        }

        public Builder addRole(Grantee grantee) {
            roles.add(grantee.getName());
            return this;
        }

        public Builder addAdmin(Grantee grantee) {
            admins.add(grantee.getName());
            return this;
        }

        @Override
        public PostgreSqlUserOptions build() {
            return new PostgreSqlUserOptions(this);
        }
    }
}
