package ua.com.nov.model.entity.metadata;

import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;

public abstract class MetaData<I extends MetaDataId> implements Unique<I>, Persistent {
    private final I id;
    private final String type;
    private String viewName;
    private String newName; // This field uses for renaming metadata
    private final Optional<? extends MetaData> options;

    public MetaData(I id, String type, Optional<? extends MetaData> options) {
        this.id = id;
        if (type != null) {
            if (type.toUpperCase().contains(id.getMdName())) {
                this.type = type;
            } else {
                this.type = type + " " + id.getMdName();
            }
        } else {
            this.type = id.getMdName();
        }
        checkMatch(id, options);
        this.options = options;
    }

    private boolean checkMatch(I eId, Optional<? extends MetaData> options) {
        if (options != null) {
            if (options.getDbClass() != eId.getDb().getClass())
                throw new IllegalArgumentException("Mismatch between database and options class.");
        }
        return true;
    }

    public MetaData(I id, String type) {
        this(id, type, null);
    }

    @Override
    public I getId() {
        return id;
    }

    public String getName() {
        return id.getName();
    }

    public String getType() {
        return type;
    }

    public String getViewName() {
        return viewName;
    }

    protected void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getNewName() {
        return getId().getDb().convert(newName);
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    @Override
    public Optional<? extends MetaData> getOptions() {
        return options;
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
    public String getCreateStmtDefinition(String conflictOption) {
        StringBuilder sb = new StringBuilder(type).append(' ');
        if (conflictOption != null) sb.append(conflictOption).append(' ');
        sb.append(id.getFullName());
        if (options != null)
            sb.append(' ').append(options);
        return sb.toString();
    }

    @Override
    public String toString() {
        return getCreateStmtDefinition(null);
    }


}
