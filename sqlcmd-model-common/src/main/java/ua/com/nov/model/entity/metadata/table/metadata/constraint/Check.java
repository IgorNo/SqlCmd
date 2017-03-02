package ua.com.nov.model.entity.metadata.table.metadata.constraint;

import ua.com.nov.model.entity.metadata.table.metadata.MetaDataId;

public class Check extends Constraint{

    private final String expression;

    public Check(MetaDataId id, String expression) {
        super(id);
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return super.toString() + " CHECK(" + expression + ')';
    }
}
