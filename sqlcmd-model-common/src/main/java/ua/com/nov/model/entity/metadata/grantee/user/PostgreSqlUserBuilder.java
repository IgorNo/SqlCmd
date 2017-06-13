package ua.com.nov.model.entity.metadata.grantee.user;

import ua.com.nov.model.entity.metadata.grantee.Grantee;

public class PostgreSqlUserBuilder extends User.Builder {

    public PostgreSqlUserBuilder(User.Id id, PostgreSqlUserOptions options) {
        super(id, options);
    }

    @Override
    public User build() {
        PostgreSqlUserOptions.Builder builder = new PostgreSqlUserOptions.Builder((PostgreSqlUserOptions) options);
        for (Grantee grantee : grantees) {
            builder.addRole(grantee);
        }
        options = builder.build();
        return new User(this);
    }
}