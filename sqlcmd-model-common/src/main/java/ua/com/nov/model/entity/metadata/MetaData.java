package ua.com.nov.model.entity.metadata;

import ua.com.nov.model.entity.MetaDataOptions;
import ua.com.nov.model.entity.Optionable;
import ua.com.nov.model.entity.Unique;

public abstract class MetaData<K extends MetaDataId> implements Unique<K>, Optionable {
    private final K id;
    private final String type;
    private final MetaDataOptions mdOptions;
    private String newName; // This field uses for renaming metadata

    public MetaData(K id, String type, MetaDataOptions options) {
        this.id = id;
        this.mdOptions = options;
        this.type = type;
    }

    public MetaData(K id, MetaDataOptions mdOptions) {
       this(id, null, mdOptions);
    }

    @Override
    public K getId() {
        return id;
    }

    public String getName() {
        return id.getName();
    }

    public String getType() {
        return type;
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
        StringBuilder sb = new StringBuilder();
        if (type != null)
            sb.append(type).append(' ');
        sb.append(id.getMdName()).append(' ');
        if (mdOptions != null && mdOptions.getExistOptions() != null)
            sb.append(mdOptions.getExistOptions()).append(' ');
        sb.append(id.getFullName()).append(" %s");
        if (mdOptions != null)
            sb.append(mdOptions);
        return sb.toString();
    }
}
