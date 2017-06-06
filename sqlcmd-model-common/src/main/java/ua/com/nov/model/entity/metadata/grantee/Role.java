package ua.com.nov.model.entity.metadata.grantee;

import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.server.Server;

public class Role extends Grantee<Role.Id> {

    public Role(Id id, Optional<Role> options) {
        super(id, options);
    }

    public static class Id extends Grantee.Id {

        public Id(Server.Id containerId, String name) {
            super(containerId, name);
        }

        @Override
        public String getMdName() {
            return "ROLE";
        }
    }
}
