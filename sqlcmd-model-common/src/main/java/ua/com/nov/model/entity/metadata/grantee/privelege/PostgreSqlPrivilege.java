package ua.com.nov.model.entity.metadata.grantee.privelege;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;
import ua.com.nov.model.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

import static ua.com.nov.model.entity.metadata.grantee.privelege.PostgreSqlPrivilege.Level.*;

public class PostgreSqlPrivilege extends Privilege {

    private PostgreSqlPrivilege(Builder builder) {
        super(builder);
    }

    public enum Action {
        ALL("ALL PRIVILEGES", COLUMN, TABLE, SCHEMA, DATABASE, FUNCTION, SEQUENCE, DOMAIN, FOREIGN_DATA_WRAPPER,
                FOREIGN_SERVER, LANGUAGE, TYPE),
        CREATE("CREATE", SCHEMA, DATABASE),
        CONNECT("CONNECT", DATABASE),
        DELETE("DELETE", TABLE),
        EXECUTE("EXECUTE", FUNCTION),
        INSERT("INSERT", COLUMN, TABLE),
        REFERENCES("REFERENCES", COLUMN, TABLE),
        SELECT("SELECT", COLUMN, TABLE, SEQUENCE),
        TEMP("TEMP", DATABASE),
        TRIGGER("TRIGGER", TABLE),
        TRUNCATE("TRUNCATE", TABLE),
        USAGE("USAGE", SEQUENCE, DOMAIN, FOREIGN_DATA_WRAPPER, FOREIGN_SERVER, LANGUAGE, TYPE);

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
        COLUMN, TABLE, SCHEMA, DATABASE, FUNCTION, SEQUENCE, DOMAIN, FOREIGN_DATA_WRAPPER, FOREIGN_SERVER,
        LANGUAGE, LARGE_OBJECT, TABLESPACE, TYPE
    }

    public static class Builder extends Privilege.Builder {
        private Level level;

        public Builder(Privilege privilege) {
            super(privilege);
        }

        public Builder(Level level, Action... actions) {
            this.level = level;
            for (Action action : actions) {
                addAction(action.toString());
            }
        }

        public static Builder creteColumnPrivileges(Table table, Action... actions) {
            Builder builder = new Builder(COLUMN, actions);
            builder.onExpression(table.getFullName());
            return builder;
        }

        public static Builder createTablePrivileges(Schema schema, Action... actions) {
            Builder builder = new Builder(TABLE, actions);
            builder.onExpression("ALL TABLES IN SCHEMA" + schema.getFullName());
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

        public static Builder creteSchemaPrivileges(Schema schema, Action... actions) {
            Builder builder = new Builder(SCHEMA, actions);
            builder.onExpression("SCHEMA " + schema.getFullName());
            return builder;
        }

        public static Builder creteDatabasePrivileges(Database database, Action... actions) {
            Builder builder = new Builder(DATABASE, actions);
            builder.onExpression("DATABASE " + database.getName());
            return builder;
        }

        private Builder addAction(Action action) {
            if (level == COLUMN)
                throw new IllegalArgumentException(
                        "For 'COLUMN' level privileges must be used method 'addAction(action, column)'.\n");
            if (action.isLevel(level))
                super.addAction(action.toString());
            else
                throw new IllegalArgumentException(String.format("Action '%s' can't be granted for level '%s'.\n",
                        action, level));
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
        public PostgreSqlPrivilege build() {
            return new PostgreSqlPrivilege(this);
        }
    }
}
