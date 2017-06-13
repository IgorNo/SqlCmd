package ua.com.nov.model.entity.metadata.grantee.user;

import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.server.Server;

public class User extends Grantee {

    public User(Builder builder) {
        super(builder);
    }

    public static class Id extends Grantee.Id {
        public Id(Server.Id dbId, String name) {
            super(dbId, name);
        }

        @Override
        public String getMdName() {
            return "USER";
        }
    }

    public static class Builder extends Grantee.Builder<User> {

        public Builder(Grantee.Id id, UserOptions options) {
            super(id, options);
        }

        @Override
        public User build() {
            return new User(this);
        }
    }
}
