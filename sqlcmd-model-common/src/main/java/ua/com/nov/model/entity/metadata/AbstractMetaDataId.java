package ua.com.nov.model.entity.metadata;

import ua.com.nov.model.entity.Child;

public abstract class AbstractMetaDataId<C> implements Child<C> {
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

    @Override
    public C getContainerId() {
        return containerId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractMetaDataId)) return false;

        AbstractMetaDataId abstractMetaDataId = (AbstractMetaDataId) o;

        if (!this.containerId.equals(abstractMetaDataId.containerId)) return false;
        return name.equalsIgnoreCase(abstractMetaDataId.name);
    }

    @Override
    public int hashCode() {
        int result = containerId.hashCode();
        result = 31 * result + name.toLowerCase().hashCode();
        return result;
    }
}
