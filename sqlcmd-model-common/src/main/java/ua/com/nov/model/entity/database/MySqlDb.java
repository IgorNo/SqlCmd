package ua.com.nov.model.entity.database;

import ua.com.nov.model.statement.Executable;
import ua.com.nov.model.statement.MySqlExecutor;

public class MySqlDb extends Database {
    private static final Executable EXECUTOR = new MySqlExecutor();

    public MySqlDb(DatabasePK pk) {
        super(pk);
    }

    public MySqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public MySqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public MySqlDb(DatabasePK pk, String password) {
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

