package ua.com.nov.model.entity.metadata.grantee.user;

import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.server.Server;

public class User extends Grantee {

    public User(Builder builder) {
        super(builder);
    }

    public String getPassword() {
        if (getOptions() != null) return ((UserOptions) getOptions()).getPassword();
        else return "";
    }

    @Override
    public String getCreateStmtDefinition(String conflictOption) {
        StringBuilder sb = new StringBuilder(getMdName()).append(' ');
        sb.append(getName());
        if (getOptions() != null)
            sb.append(getOptions());
        return sb.toString();
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

    public abstract static class Builder extends Grantee.Builder<User> {

        public Builder(Server.Id id, String name, UserOptions options) {
            super(id, name, options);
        }
    }
}
