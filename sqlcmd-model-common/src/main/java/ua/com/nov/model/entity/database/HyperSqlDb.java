package ua.com.nov.model.entity.database;

public class HyperSqlDb extends Database {

    public HyperSqlDb(DatabaseID pk) {
        super(pk);
    }

    public HyperSqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public HyperSqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public HyperSqlDb(DatabaseID pk, String password) {
        super(pk, password);
    }

    @Override
    public Executable getExecutor() {
        return new HyperSqlExecutor();
    }

    @Override
    public String getDbProperties() {
        return "";
    }

    public class HyperSqlExecutor extends Executor {
    }

}
