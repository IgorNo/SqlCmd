package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;

public abstract class Constraint extends TableMd<TableMd.Id> {

    protected Constraint(Id id, Builder builder) {
        super(id, builder);
    }

    @Override
    public String getCreateStmtDefinition(String conflictOption) {
        StringBuilder sb = new StringBuilder();
        if (getName() != null && !getName().isEmpty())
            sb.append(getId().getMdName()).append(' ').append(getId().getName()).append(' ');
        sb.append(getType()).append(" %s");
        if (getOptions() != null)
            sb.append('\n').append(getOptions());
        return sb.toString();
    }

    public abstract static class Id extends TableMd.Id {
        public Id(Table.Id tableId, String name) {
            super(tableId, name);
        }

        @Override
        public String getMdName() {
            return "CONSTRAINT";
        }

        @Override
        public String getFullName() {
            return getName();
        }
    }

}
