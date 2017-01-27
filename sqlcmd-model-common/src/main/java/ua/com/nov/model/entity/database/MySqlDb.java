package ua.com.nov.model.entity.database;

public class MySqlDb extends Database {

    public MySqlDb(DatabaseID pk) {
        super(pk);
    }

    public MySqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public MySqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public MySqlDb(DatabaseID pk, String password) {
        super(pk, password);
    }

    @Override
    public Executable getExecutor() {
        return new MySqlExecutor();
    }

    private class MySqlExecutor extends Executor {
    }
}

