package ua.com.nov.model.entity.metadata.grantee;

import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.server.Server;

public class User extends Grantee<User.Id> {

    public User(Id id, Optional<User> options) {
        super(id, options);
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
}
