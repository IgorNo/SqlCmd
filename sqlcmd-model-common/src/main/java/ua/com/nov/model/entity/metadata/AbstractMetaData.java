package ua.com.nov.model.entity.metadata;

import ua.com.nov.model.entity.Unique;

public abstract class AbstractMetaData<K extends AbstractMetaDataId> implements Unique<K> {
    private final K id;
    private String alterExpression;
    private String newName; // This field uses for renaming metadata

    public AbstractMetaData(K id) {
        this.id = id;
        newName = id.getName();
    }

    @Override
    public K getId() {
        return id;
    }

    public String getName() {
        return getId().getName();
    }

    public String getAlterExpression() {
        return alterExpression;
    }

    public void setAlterExpression(String alterExpression) {
        this.alterExpression = alterExpression;
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
        if (!(o instanceof AbstractMetaData)) return false;

        AbstractMetaData<?> metaData = (AbstractMetaData<?>) o;

        return id.equals(metaData.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
