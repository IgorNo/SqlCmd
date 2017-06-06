package ua.com.nov.model.entity.metadata;

import ua.com.nov.model.entity.Hierarchical;
import ua.com.nov.model.entity.metadata.server.Server;

public class NullMetaData implements Hierarchical {

    @Override
    public Server getServer() {
        return null;
    }

    @Override
    public String getMdName() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getFullName() {
        return null;
    }

    @Override
    public Object getContainerId() {
        return null;
    }
}
