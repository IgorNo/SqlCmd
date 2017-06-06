package ua.com.nov.model.entity.metadata;

import ua.com.nov.model.entity.Hierarchical;
import ua.com.nov.model.entity.metadata.server.Server;

public abstract class MetaDataId<C extends Hierarchical> implements Hierarchical<C> {
    private final C containerId;
    private final String name;

    public MetaDataId(C containerId, String name) {
        if (containerId == null) {
            throw new IllegalArgumentException("Meta data containerId can't be 'null'.");
        }
        this.containerId = containerId;
        this.name = name;
    }

    public abstract String getMdName();

    @Override
    public C getContainerId() {
        return containerId;
    }

    @Override
    public Server getServer() {
        return containerId.getServer();
    }

    @Override
    public String getFullName() {
        return getServer().convert(containerId.getFullName() + "." + name);
    }

    public String getName() {
        return getServer().convert(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof MetaDataId<?>)) return false;

        MetaDataId<?> that = (MetaDataId<?>) o;

        if (!getMdName().equals(that.getMdName())) return false;
        if (!containerId.equals(that.containerId)) return false;
        return getFullName().equals(that.getFullName());
    }

    @Override
    public int hashCode() {
        int result = getMdName().hashCode();
        result = 31 * result + containerId.hashCode();
        result = 31 * result + getFullName().hashCode();
        return result;
    }
}
