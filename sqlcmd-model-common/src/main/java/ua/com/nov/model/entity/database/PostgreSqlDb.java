package ua.com.nov.model.entity.database;

import ua.com.nov.model.statement.Executable;
import ua.com.nov.model.statement.PostgreSqlExecutor;

public class PostgreSqlDb extends Database {

    private static final Executable EXECUTOR = new PostgreSqlExecutor();

    public PostgreSqlDb(DatabasePK pk) {
        super(pk);
    }

    public PostgreSqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public PostgreSqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public PostgreSqlDb(DatabasePK pk, String password) {
        super(pk, password);
    }

    @Override
    public Executable getExecutor() {
        return EXECUTOR;
    }

    @Override
    public String getProperties() {
        return "";
    }
}
