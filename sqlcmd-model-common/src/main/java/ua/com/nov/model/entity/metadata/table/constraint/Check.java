package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMd;

public class Check extends Constraint {
    private final String expression;

    public static class Builder extends TableMd.Builder {
        private final String expression;

        public Builder(TableId tableId, String name, String expression) {
            super(tableId, name);
            this.expression = expression;
        }

        public Builder(String name, String expression) {
            this(null, name, expression);
        }

        public Check build() {
            return new Check(this);
        }
    }

    public Check(Builder builder) {
        super(builder);
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
