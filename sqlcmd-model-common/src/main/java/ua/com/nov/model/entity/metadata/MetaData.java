package ua.com.nov.model.entity.metadata;

import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.Persistent;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.database.Database;

public abstract class MetaData<I extends MetaDataId> implements Unique<I>, Persistent {
    private final I id;
    private final String type;
    private String viewName;
    private Optional<? extends MetaData> options;

    public MetaData(I id, String type, Optional<? extends MetaData> options) {
        this.id = id;
        if (type != null) {
            this.type = type.toUpperCase();
            this.type.replaceAll(id.getMdName(), "").trim();
        } else {
            this.type = null;
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

    @Override
    public I getId() {
        return id;
    }

    public String getName() {
        return id.getName();
    }

    public String getFullName() {
        return id.getFullName();
    }

    public String getMdName() {
        return id.getMdName();
    }

    public Database getDb() {
        return id.getDb();
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

    @Override
    public Optional<? extends MetaData> getOptions() {
        return options;
    }

    protected void setOptions(Optional<? extends MetaData> options) {
        this.options = options;
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
        StringBuilder sb = new StringBuilder();
        if (type != null) sb.append(type).append(' ');
        sb.append(getMdName()).append(' ');
        if (conflictOption != null) sb.append(conflictOption).append(' ');
        sb.append(getFullName()).append("%s");
        if (options != null)
            sb.append('\n').append(options);
        return sb.toString();
    }

    @Override
    public String toString() {
        return getCreateStmtDefinition(null);
    }


}
