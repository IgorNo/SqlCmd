package ua.com.nov.model.entity.metadata.grantee.user;

import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.grantee.privelege.MySqlPrivilege;
import ua.com.nov.model.entity.metadata.grantee.privelege.Privilege;

public class MySqlUserBuilder extends User.Builder {

    public MySqlUserBuilder(User.Id id, MySqlUserOptions options) {
        super(id, options);
    }

    @Override
    public User build() {
        for (Grantee grantee : grantees) {
            for (Privilege privilege : grantee.getAllPrivileges()) {
                if (privilege.isWithGrantOptions())
                    addPrivelege(new MySqlPrivilege.Builder((MySqlPrivilege) privilege));
            }
        }
        return new User(this);
    }
}