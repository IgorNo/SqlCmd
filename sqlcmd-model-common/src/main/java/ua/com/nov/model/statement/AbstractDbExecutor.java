package ua.com.nov.model.statement;

public abstract class AbstractDbExecutor implements Executable {

    public static final String CREATE_DB_SQL = "CREATE DATABASE %s %s";
    public static final String DROP_DB_SQL = "DROP DATABASE %s";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE %s (%s)";
    public static final String DROP_TABLE_SQL = "DROP TABLE %s";
    public static final String RENAME_TABLE_SQL = "ALTER TABLE %s RENAME TO %s";


    @Override
    public String getCreateDbStmt() {
        return CREATE_DB_SQL;
    }

    @Override
    public String getDropDbStmt() {
        return DROP_DB_SQL;
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
        return CREATE_TABLE_SQL;
    }

    @Override
    public String getDropTableStmt() {
        return DROP_TABLE_SQL;
    }

    @Override
    public String getUpdateTableStmt() {
        return RENAME_TABLE_SQL;
    }
}
