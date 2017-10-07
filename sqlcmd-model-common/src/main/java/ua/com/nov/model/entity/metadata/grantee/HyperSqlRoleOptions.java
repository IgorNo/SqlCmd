package ua.com.nov.model.entity.metadata.grantee;

import ua.com.nov.model.entity.metadata.grantee.user.UserOptions;
import ua.com.nov.model.entity.metadata.server.HsqldbServer;
import ua.com.nov.model.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

public class HyperSqlRoleOptions extends UserOptions {

    private HyperSqlRoleOptions(Builder builder) {
        super(builder);
    }

    @Override
    public String getCreateOptionsDefinition() {
        return CollectionUtils.toString(getOptionsMap(), " ", " ");
    }

    @Override
    public List<String> getUpdateOptionsDefinition() {
        return new LinkedList<>();
    }

    public enum Grantor {
        CURRENT_USER, CURRENT_ROLE
    }

    public abstract static class Builder extends UserOptions.Builder {
        public Builder() {
            super(HsqldbServer.class);
        }

        public Builder withAdmin(Grantor grantor) {
            addOption("WITH ADMIN", grantor.toString());
            return this;
        }

        @Override
        public HyperSqlRoleOptions build() {
            return new HyperSqlRoleOptions(this);
        }
    }
}
