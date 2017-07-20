package ua.com.nov.model.entity.metadata;

import ua.com.nov.model.entity.MetaData;
import ua.com.nov.model.entity.MetaDataId;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.Unique;
import ua.com.nov.model.entity.metadata.server.Server;

public abstract class AbstractMetaData<I extends AbstractMetaData.Id> implements Unique<I>, MetaData {
    private final I id;
    private final String type;
    private String viewName;
    private Optional<? extends AbstractMetaData> options;

    public AbstractMetaData(I id, String type, Optional<? extends AbstractMetaData> options) {
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

    private boolean checkMatch(I eId, Optional<? extends AbstractMetaData> options) {
        if (options != null) {
            if (options.getServerClass() != eId.getServer().getClass())
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

    public Server getServer() {
        return id.getServer();
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
    public Optional<? extends AbstractMetaData> getOptions() {
        return options;
    }

    protected void setOptions(Optional<? extends AbstractMetaData> options) {
        this.options = options;
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

    @Override
    public String getCreateStmtDefinition(String conflictOption) {
        StringBuilder sb = new StringBuilder();
        if (type != null) sb.append(type).append(' ');
        sb.append(getMdName()).append(' ');
        if (conflictOption != null) sb.append(conflictOption).append(' ');
        sb.append(getFullName()).append("%s");
        if (options != null)
            sb.append(options);
        return sb.toString();
    }

    @Override
    public String toString() {
        return getCreateStmtDefinition(null);
    }


    public abstract static class Id<C extends MetaDataId> implements MetaDataId<C> {
        private final C containerId;
        private final String name;

        public Id(C containerId, String name) {
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
            if (o == null || !(o instanceof AbstractMetaData.Id<?>)) return false;

            Id<?> that = (Id<?>) o;

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

        @Override
        public String toString() {
            return getFullName();
        }
    }
}
