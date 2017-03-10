package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.TableMdId;

public abstract class Constraint extends TableMd {

    private static class ConstraintId extends TableMdId {
        public ConstraintId(TableId containerId, String name) {
            super(containerId, name);
        }
    }

    public Constraint(Builder builder) {
        super(new ConstraintId(builder.getTableId(), builder.getName()));
    }

    @Override
    public String toString() {
        String result = "";
        if (getId().getName() != null) result += "CONSTRAINT " + getId().getName();
        return result;
    }
}
