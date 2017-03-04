package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.TableMdId;

public abstract class Constraint extends TableMd {

    public Constraint(TableMdId id) {
        super(id);
    }

    public Constraint(TableId id, String name) {
        this(new TableMdId(id, name));
    }

    @Override
    public String toString() {
        String result = "";
        if (getId().getName() != null) result += "CONSTRAINT " + getId().getName();
        return result;
    }
}
