package ua.com.nov.model.entity.database;

import ua.com.nov.model.statement.Executable;
import ua.com.nov.model.statement.HyperSqlExecutor;

public class HyperSqlDb extends Database {
    private static final Executable EXECUTOR = new HyperSqlExecutor();

    public HyperSqlDb(DatabasePK pk) {
        super(pk);
    }

    public HyperSqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public HyperSqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public HyperSqlDb(DatabasePK pk, String password) {
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
