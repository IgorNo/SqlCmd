package ua.com.nov.model.entity.metadata;

import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.metadata.database.Database;

public abstract class MetaDataId<C extends Persistent> implements Persistent<C> {
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
    public Database getDb() {
        return containerId.getDb();
    }

    @Override
    public String getFullName() {
        return getDb().convert(containerId.getFullName() + "." + name);
    }

    public String getName() {
        return getDb().convert(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof MetaDataId<?>)) return false;

        MetaDataId<?> that = (MetaDataId<?>) o;

        if (!containerId.equals(that.containerId)) return false;
        return getFullName().equals(that.getFullName());
    }

    @Override
    public int hashCode() {
        int result = containerId.hashCode();
        result = 31 * result + getFullName().hashCode();
        return result;
    }
}
