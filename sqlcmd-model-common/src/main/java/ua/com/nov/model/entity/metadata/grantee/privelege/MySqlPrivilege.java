package ua.com.nov.model.entity.metadata.grantee.privelege;

import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.grantee.Grantee;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.Column;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static ua.com.nov.model.entity.metadata.grantee.privelege.MySqlPrivilege.Level.*;

public final class MySqlPrivilege extends Privilege {

    private MySqlPrivilege(Builder builder) {
        super(builder);
    }

    public enum Level {
        GLOBAL, DATABASE, TABLE, COLUMN, PROCEDURE
    }

    public enum Action {
        ALL("ALL PRIVILEGES", GLOBAL, DATABASE, TABLE, COLUMN, PROCEDURE),
        ALTER("ALTER", GLOBAL, DATABASE, TABLE),
        ALTER_ROUTINE("ALTER ROUTINE", GLOBAL, DATABASE, PROCEDURE),
        CREATE("CREATE", GLOBAL, DATABASE, TABLE),
        CREATE_ROUTINE("CREATE ROUTINE", GLOBAL, DATABASE),
        CREATE_TABLESPACE("CREATE TABLESPACE", GLOBAL),
        CREATE_TEMPORARY_TABLES("CREATE TEMPORARY TABLES", GLOBAL),
        CREATE_USER("CREATE USER", GLOBAL),
        CREATE_VIEW("CREATE VIEW", GLOBAL, DATABASE, TABLE),
        DELETE("DELETE", GLOBAL, DATABASE, TABLE),
        DROP("DROP", GLOBAL, DATABASE, TABLE),
        EVENT("EVENT", GLOBAL, DATABASE),
        EXECUTE("EXECUTE", GLOBAL, DATABASE, TABLE),
        FILE("FILE", GLOBAL),
        GRANT_OPTION("GRANT OPTION", GLOBAL, DATABASE, TABLE, PROCEDURE),
        INDEX("INDEX", GLOBAL, DATABASE, TABLE),
        INSERT("INSERT", GLOBAL, DATABASE, TABLE, COLUMN),
        LOCK_TABLES("LOCK TABLES", GLOBAL, DATABASE),
        PROCESS("PROCESS", GLOBAL),
        REFERENCES("REFERENCES", GLOBAL, DATABASE, TABLE, COLUMN),
        RELOAD("RELOAD", GLOBAL),
        REPLICATION_CLIENT("REPLICATION CLIENT", GLOBAL),
        REPLICATION_SLAVE("REPLICATION SLAVE", GLOBAL),
        SELECT("SELECT", GLOBAL, DATABASE, TABLE, COLUMN),
        SHOW_DATABASES("SHOW DATABASES", GLOBAL),
        SHOW_VIEW("SHOW VIEW", GLOBAL, DATABASE, TABLE),
        SHUTDOWN("SHUTDOWN", GLOBAL),
        SUPER("SUPER", GLOBAL),
        TRIGGER("TRIGGER", GLOBAL, DATABASE, TABLE),
        UPDATE("UPDATE", GLOBAL, DATABASE, TABLE, COLUMN),
        USAGE("USAGE");

        private String action;
        private List<Level> levelList = new LinkedList<>();

        Action(String action, Level... levels) {
            this.action = action;
            Collections.addAll(levelList, levels);
        }

        public boolean isLevel(Level level) {
            return levelList.contains(level);
        }

        @Override
        public String toString() {
            return action;
        }
    }

    public static class Builder extends Privilege.Builder {
        private Level level;

        public Builder(MySqlPrivilege privilege) {
            super(privilege);
        }

        private Builder(Level level, Action... actions) {
            this.level = level;
            for (Action action : actions) {
                addAction(action);
            }
        }

        public static Builder createGlobalPrivileges(Action... actions) {
            Builder builder = new Builder(GLOBAL, actions);
            builder.onExpression("*.*");
            return builder;
        }

        public static Builder createDatabasePrivileges(Database db, Action... actions) {
            Builder builder = new Builder(DATABASE, actions);
            builder.onExpression(db.getName() + ".*");
            return builder;
        }

        public static Builder createTablePrivileges(Table table, Action... actions) {
            Builder builder = new Builder(TABLE, actions);
            builder.onExpression(table.getFullName());
            return builder;
        }

        public static Builder createColumnPrivileges(Table table) {
            Builder builder = new Builder(COLUMN);
            builder.onExpression(table.getFullName());
            return builder;
        }

        private Builder addAction(Action action) {
            if (level == COLUMN)
                throw new IllegalArgumentException(
                        "For 'COLUMN' level privileges must be used method 'addAction(action, column)'.");

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
        public MySqlPrivilege build() {
            return new MySqlPrivilege(this);
        }
    }
}
