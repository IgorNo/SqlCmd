package ua.com.nov.model.entity.metadata.grantee;

import ua.com.nov.model.entity.Buildable;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.grantee.privelege.Privilege;
import ua.com.nov.model.entity.metadata.server.Server;

import java.util.*;

public abstract class Grantee extends MetaData<Grantee.Id> {
    private final List<Privilege> privileges = new ArrayList<>();
    private Set<Grantee> grantees;

    protected Grantee(Builder builder) {
        super(builder.id, null, builder.options);
        if (!builder.grantees.isEmpty()) this.grantees = builder.grantees;
        for (Object privilegeBuilder : builder.privilegeBuilders) {
            this.privileges.add(((Privilege.Builder) privilegeBuilder).addGrantee(this).build());
        }
    }

    public List<Privilege> getAllPrivileges() {
        List<Privilege> result = new ArrayList<>(privileges);
        for (Grantee grantee : grantees) {
            if (grantee.grantees != null) result.addAll(getAllPrivileges());
        }
        return result;
    }

    public List<Grantee> getGranteeList() {
        return new ArrayList<>(grantees);
    }

    public List<Privilege> getPrivilegeList() {
        return Collections.unmodifiableList(privileges);
    }

    @Override
    public String getCreateStmtDefinition(String conflictOption) {
        return String.format(super.getCreateStmtDefinition(null), "");
    }

    public abstract static class Id extends MetaDataId<Server.Id> {

        public Id(Server.Id containerId, String name) {
            super(containerId, name);
        }

        @Override
        public String getFullName() {
            return getName();
        }
    }

    public abstract static class Builder<T extends Grantee> implements Buildable<T> {
        protected GranteeOptions<? extends Grantee> options;
        protected Set<Grantee> grantees = new HashSet<>();
        protected List<Privilege.Builder> privilegeBuilders = new ArrayList<>();
        private Grantee.Id id;

        public Builder(Grantee.Id id, GranteeOptions<? extends Grantee> options) {
            this.id = id;
            this.options = options;
        }

        public Builder addGrantee(Grantee grantee) {
            grantees.add(grantee);
            return this;
        }

        public Builder addPrivelege(Privilege.Builder privilege) {
            privilegeBuilders.add(privilege);
            return this;
        }
    }

}
