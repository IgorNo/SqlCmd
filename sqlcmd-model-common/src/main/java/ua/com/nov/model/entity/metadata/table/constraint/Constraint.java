package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.TableMdId;

public abstract class Constraint extends TableMd {

    public Constraint(TableMdId id) {
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
}
