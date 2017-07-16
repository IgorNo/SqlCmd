package ua.com.nov.model.entity.metadata.grantee.privelege;

import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

import static ua.com.nov.model.entity.metadata.grantee.privelege.HyperSqlPrivilege.Level.*;

public class HyperSqlPrivilege extends Privilege {

    public HyperSqlPrivilege(Builder builder) {
        super(builder);
    }

    public enum Action {
        ALL("ALL PRIVILEGES", COLUMN, TABLE, FUNCTION, PROCEDURE, SEQUENCE, DOMAIN, COLLATION,
                CHARACTER_SET, ROUTINE, TYPE),
        DELETE("DELETE", TABLE),
        EXECUTE("EXECUTE", FUNCTION),
        INSERT("INSERT", COLUMN, TABLE),
        REFERENCES("REFERENCES", COLUMN, TABLE),
        SELECT("SELECT", COLUMN, TABLE, SEQUENCE),
        TRIGGER("TRIGGER", TABLE),
        TRUNCATE("TRUNCATE", TABLE),
        UPDATE("UPDATE", COLUMN, TABLE),
        USAGE("USAGE", SEQUENCE, DOMAIN, TYPE);


        private String action;
        private List<Level> levelList = new LinkedList<>();

        Action(String action, Level... levels) {
            this.action = action;
            for (Level level : levels) {
                levelList.add(level);
            }
        }

        public boolean isLevel(Level level) {
            return levelList.contains(level);
        }


        @Override
        public String toString() {
            return action;
        }
    }

    public enum Level {
        COLUMN, TABLE, FUNCTION, PROCEDURE, SEQUENCE, DOMAIN, COLLATION, CHARACTER_SET, ROUTINE, TYPE
    }

    public static class Builder extends Privilege.Builder {
        private Level level;

        public Builder(HyperSqlPrivilege privilege) {
            super(privilege);
        }

        private Builder(Level level, Action... actions) {
            this.level = level;
            for (Action action : actions) {
                addAction(action.toString());
            }
        }

        public static Builder creteColumnPrivileges(Table table) {
            Builder builder = new Builder(COLUMN);
            builder.onExpression(table.getFullName());
            return builder;
        }

        public static Builder createTablePrivileges(Table table, Action... actions) {
            Builder builder = new Builder(TABLE, actions);
            builder.onExpression(table.getFullName());
            return builder;
        }

        public static Builder createTablePrivileges(Table[] tables, Action... actions) {
            Builder builder = new Builder(TABLE, actions);
            StringBuilder sb = new StringBuilder();
            builder.onExpression(CollectionUtils.toString(tables));
            return builder;
        }

        private Builder addAction(Action... actions) {
            if (level == COLUMN)
                throw new IllegalArgumentException(
                        "For 'COLUMN' level privileges must be used method 'addAction(action, column)'.\n");
            for (Action action : actions) {
                if (action.isLevel(level))
                    super.addAction(action.toString());
                else
                    throw new IllegalArgumentException(String.format("Action '%s' can't be granted for level '%s'.\n",
                            action, level));
            }
            return this;
        }

        public Builder addAction(Action action, Column... columns) {
            if (action.isLevel(COLUMN))
                super.addAction(action.toString(), columns);
            else
                throw new IllegalArgumentException(String.format("Action '%s' can't be granted for 'COLUMN' level.\n",
                        action));
            return this;
        }

        public Builder addUser(Grantee user) {
            super.addGrantee(user);
            return this;
        }

        @Override
        public HyperSqlPrivilege build() {
            return new HyperSqlPrivilege(this);
        }
    }
}
