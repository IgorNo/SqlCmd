package ua.com.nov.model.entity.metadata.grantee;

import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.MetaData;
import ua.com.nov.model.entity.metadata.MetaDataId;
import ua.com.nov.model.entity.metadata.server.Server;

import java.util.*;

public class Grantee<I extends Grantee.Id> extends MetaData<I> {
    private Map<String, ? extends Grantee> grantees = new HashMap<>();
    private Map<? extends MetaData, Set<String>> privileges = new HashMap<>();

    public Grantee(I id, Optional<? extends Grantee> options) {
        super(id, null, options);
    }

    public List<? extends Grantee> getGrantees() {
        return new LinkedList<>(grantees.values());
    }

    public Map<? extends MetaData, Set<String>> getAllPrivileges() {
        return Collections.unmodifiableMap(privileges);
    }

    public Collection<String> getPrivileges(MetaData<?> metaData) {
        return null;
    }

    public abstract static class Id extends MetaDataId<Server.Id> {

        public Id(Server.Id containerId, String name) {
            super(containerId, name);
        }
    }
}
