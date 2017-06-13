package ua.com.nov.model.entity.metadata.grantee.user;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.metadata.grantee.GranteeOptions;
import ua.com.nov.model.entity.metadata.server.Server;

public abstract class UserOptions extends GranteeOptions<User> {
    private String password;

    public UserOptions(Builder builder) {
        super(builder);
        this.password = builder.password;
    }

    public String getPassword() {
        return password;
    }

    public abstract static class Builder<T extends UserOptions> extends MetaDataOptions.Builder<T> {
        private String password;

        public Builder(Class<? extends Server> serverClass) {
            super(serverClass);
        }

        public Builder(UserOptions that) {
            super(that);
            this.password = that.password;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }
    }

}
