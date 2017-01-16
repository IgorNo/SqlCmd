package ua.com.nov.model.entity.database;

public class PostgreSqlDb extends Database {

    public PostgreSqlDb(DatabaseID pk) {
        super(pk);
    }

    public PostgreSqlDb(String dbUrl, String userName) {
        super(dbUrl, userName);
    }

    public PostgreSqlDb(String dbUrl, String userName, String password) {
        super(dbUrl, userName, password);
    }

    public PostgreSqlDb(DatabaseID pk, String password) {
        super(pk, password);
    }

    @Override
    public Executable getExecutor() {
        return new PostgreSqlExecutor();
    }

    @Override
    public String getProperties() {
        return "";
    }

    private class PostgreSqlExecutor extends Executor {

        @Override
        public String getSelectAllDbStmt() {
            return "SELECT datname FROM pg_database WHERE datistemplate = false";
        }
    }

}
