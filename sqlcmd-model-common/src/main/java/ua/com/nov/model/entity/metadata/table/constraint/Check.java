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

        public Check build() {
            return new Check(this);
        }
    }

    public static class Id extends Constraint.Id {
        public Id(Table.Id containerId, String name) {
            super(containerId, name);
        }

        @Override
        public String getMdName() {
            return "CHECK";
        }

    }

    public Check(Builder builder) {
        super(new Id(builder.getTableId(), builder.getName()));
        this.expression = builder.expression;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return super.toString() + " CHECK(" + expression + ')';
    }
}
