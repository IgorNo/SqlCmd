package ua.com.nov.model.entity.metadata.grantee.user;

import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.grantee.privelege.HyperSqlPrivilege;
import ua.com.nov.model.entity.metadata.grantee.privelege.Privilege;

public class HyperSqlUserBuilder extends User.Builder {

    public HyperSqlUserBuilder(User.Id id, HyperSqlUserOptions options) {
        super(id, options);
    }

    @Override
    public User build() {
        for (Grantee grantee : grantees) {
            for (Privilege privilege : grantee.getAllPrivileges()) {
                if (privilege.isWithGrantOptions())
                    addPrivelege(new HyperSqlPrivilege.Builder((HyperSqlPrivilege) privilege));
            }
        }
        return new User(this);
    }
}