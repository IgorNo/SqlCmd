package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;

public abstract class Constraint<K extends Constraint.Id> extends TableMd<K> {

    public Constraint(K id) {
        super(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getId().getName() != null)
            sb.append("CONSTRAINT ").append(getId().getName()).append(' ');
        sb.append(getId().getMdName());
        return sb.toString();
    }

    public abstract static class Id extends TableMd.Id {
        public Id(Table.Id containerId, String name) {
            super(containerId, name);
        }

        @Override
        public String getFullName() {
            return getName();
        }
    }
}
