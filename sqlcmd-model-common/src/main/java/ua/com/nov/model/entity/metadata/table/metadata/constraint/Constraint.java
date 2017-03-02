package ua.com.nov.model.entity.metadata.table.metadata.constraint;

import ua.com.nov.model.entity.metadata.AbstractMetaData;
import ua.com.nov.model.entity.metadata.table.metadata.MetaDataId;

public abstract class Constraint extends AbstractMetaData<MetaDataId> {

    public Constraint(MetaDataId id) {
        super(id);
    }

    @Override
    public String toString() {
        String result = "";
        if (getId().getName() != null) result += "CONSTRAINT " + getId().getName();
        return result;
    }
}
