package ua.com.nov.model.entity.metadata.table.metadata.constraint;

import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.metadata.TableMdId;

public abstract class Constraint extends AbstractMetaData<TableMdId> {

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
