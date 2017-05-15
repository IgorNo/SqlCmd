package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;

public class Check extends Constraint {
    private final String expression;

    public static class Builder extends TableMd.Builder {
        private final String expression;

        public Builder(Table.Id tableId, String name, String expression) {
            super(name, tableId);
            this.expression = expression;
        }

        public Builder(String name, String expression) {
            this(null, name, expression);
        }

        @Override
        public String generateName(String postfix) {
            return getName();
        }

        public Check build() {
            if (getName() == null) setName("");
            return new Check(this);
        }
    }

    public Check(Builder builder) {
        super(builder);
        this.expression = builder.expression;
    }

    @Override
    public String getCreateStmtDefinition(String conflictOption) {
        return String.format(super.getCreateStmtDefinition(conflictOption), getExpression());
    }

    public String getExpression() {
        return "(" + expression + ")";
    }
}
