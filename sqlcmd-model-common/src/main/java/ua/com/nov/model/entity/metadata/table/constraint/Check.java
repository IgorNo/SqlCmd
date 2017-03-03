package ua.com.nov.model.entity.metadata.table.constraint;

import ua.com.nov.model.entity.metadata.table.TableId;
import ua.com.nov.model.entity.metadata.table.TableMdId;

public class Check extends Constraint{

    private final String expression;

    public Check(TableMdId id, String expression) {
        super(id);
        this.expression = expression;
    }

    public Check(TableId tableId, String checkName, String expression) {
        this(new TableMdId(tableId, checkName), expression);
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return super.toString() + " CHECK(" + expression + ')';
    }
}
