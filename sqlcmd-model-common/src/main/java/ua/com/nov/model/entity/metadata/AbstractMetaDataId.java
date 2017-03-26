package ua.com.nov.model.entity.metadata;

import ua.com.nov.model.entity.Child;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.metadata.database.Database;

public abstract class AbstractMetaDataId<C extends Persistent> implements Child<C>, Persistent{
    private final C containerId;
    private final String name;


    public AbstractMetaDataId(C containerId, String name) {
        if (containerId == null) {
            throw new IllegalArgumentException("Meta data containerId can't be 'null'.");
        }
        if (name == null || "".equals(name)) {
            throw new IllegalArgumentException("Meta data name can't be 'null' or empty.");
        }
        this.containerId = containerId;
        this.name = name;
    }

    public abstract String getMetaDataName();

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
        if (o == null || getClass() != o.getClass()) return false;

        AbstractMetaDataId<?> that = (AbstractMetaDataId<?>) o;

        if (!containerId.equals(that.containerId)) return false;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        int result = containerId.hashCode();
        
        result = 31 * result + name.toLowerCase().hashCode();
        return result;
    }
}
