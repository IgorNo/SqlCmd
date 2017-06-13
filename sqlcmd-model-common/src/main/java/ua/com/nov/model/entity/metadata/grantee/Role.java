package ua.com.nov.model.entity.metadata.grantee;

import ua.com.nov.model.entity.metadata.server.Server;

public class Role extends Grantee {

    public Role(Builder builder) {
        super(builder);
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
