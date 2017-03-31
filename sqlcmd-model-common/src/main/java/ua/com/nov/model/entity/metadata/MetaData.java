package ua.com.nov.model.entity.metadata;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.Optionable;
import ua.com.nov.model.entity.Unique;

public abstract class MetaData<K extends MetaDataId> implements Unique<K>, Optionable {
    private final K id;
    private final MetaDataOptions mdOptions;
    private String newName; // This field uses for renaming metadata

    public MetaData(K id, MetaDataOptions options) {
        this.id = id;
        this.mdOptions = options;
    }

    @Override
    public K getId() {
        return id;
    }

    public String getName() {
        return id.getName();
    }

    @Override
    public MetaDataOptions getMdOptions() {
        return mdOptions;
    }

    public String getNewName() {
        return getId().getDb().convert(newName);
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetaData)) return false;

        MetaData<?> metaData = (MetaData<?>) o;

        return id.equals(metaData.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(id.getMdName()).append(' ').append(id.getFullName());
        return sb.toString();
    }
}
