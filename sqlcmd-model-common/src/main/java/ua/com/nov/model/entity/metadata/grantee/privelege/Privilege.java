package ua.com.nov.model.entity.metadata.grantee.privelege;

import ua.com.nov.model.entity.Buildable;
import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Privilege {
    private final List<String> actions;
    private final List<Grantee> grantees;
    private final String onExpression;
    private final boolean withGrantOptions;

    public Privilege(Builder builder) {
        this.actions = builder.actions;
        this.grantees = builder.grantees;
        this.onExpression = builder.onExpression;
        this.withGrantOptions = builder.withGrantOptions;
    }

    public String getActions() {
        return CollectionUtils.toString(actions);
    }

    public String getGrantees() {
        return CollectionUtils.toString(grantees);
    }

    public List<Grantee> getGranteeList() {
        return Collections.unmodifiableList(grantees);
    }

    public String getOnExpression() {
        return onExpression;
    }

    public boolean isWithGrantOptions() {
        return withGrantOptions;
    }

    public String getCreateStmtDefinition() {
        StringBuilder sb = new StringBuilder(getActions());

        sb.append("\n\tON ").append(onExpression);

        sb.append("\n\tTO ").append(getGrantees());

        if (withGrantOptions) sb.append("\n\tWITH GRANT OPTION");

        sb.append(";\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return getCreateStmtDefinition();
    }

    public abstract static class Builder implements Buildable<Privilege> {
        private final List<String> actions;
        private List<Grantee> grantees;
        private String onExpression;
        private boolean withGrantOptions;

        protected Builder() {
            actions = new ArrayList<>();
            grantees = new ArrayList<>();
        }

        protected Builder(Privilege privilege) {
            actions = new ArrayList<>(privilege.actions);
            grantees = new ArrayList<>();
            onExpression = privilege.onExpression;
            withGrantOptions = privilege.withGrantOptions;
        }

        protected void addAction(String action) {
            actions.add(action);
        }

        protected void addAction(String action, Column... columns) {
            StringBuilder sb = new StringBuilder(action);
            String s = "";
            sb.append('(');
            for (Column column : columns) {
                if (column.getTableId().equals(onExpression))
                    sb.append(s).append(column.getName());
            }
            sb.append(')');
            actions.add(sb.toString());
        }

        public Builder addGrantee(Grantee grantee) {
            grantees.add(grantee);
            return this;
        }

        protected Builder onExpression(String onExpression) {
            this.onExpression = onExpression;
            return this;
        }

        protected Builder withGrantOptions(boolean withGrantOptions) {
            this.withGrantOptions = withGrantOptions;
            return this;
        }

    }
}
