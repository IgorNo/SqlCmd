package ua.com.nov.model.statement;

public class PostgreSqlExecutor extends AbstractDbExecutor {

    @Override
    public String getSelectAllDbStmt() {
        return "SELECT datname FROM pg_database WHERE datistemplate = false";
    }
}
