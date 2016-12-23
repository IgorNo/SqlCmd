package ua.com.nov.model.statement;

public abstract class AbstractDbExecutor implements Executable {

    @Override
    public String getCreateDbStmt() {
        return "CREATE DATABASE %1 ";
    }

    @Override
    public String getDropDbStmt() {
        return "DROP DATABASE %1";
    }

    @Override
    public String getAlterDbStmt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSelectAllDbStmt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCreateTableStmt() {
        return null;
    }

    @Override
    public String getDropTableStmt() {
        return null;
    }

    @Override
    public String getUpdateTableStmt() {
        return null;
    }
}
