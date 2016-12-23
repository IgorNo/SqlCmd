package ua.com.nov.model.entity.database;

import ua.com.nov.model.statement.Executable;
import ua.com.nov.model.util.DataSourceUtil;

public abstract class Database {
    private DatabasePK pk;
    private String password;

    public Database(DatabasePK pk) {
        this(pk, null);
    }

    public Database(String dbUrl, String userName) {
        this(new DatabasePK(dbUrl, userName), null);
    }

    public Database(String dbUrl, String userName, String password) {
        this(new DatabasePK(dbUrl, userName), password);
    }

    public Database(DatabasePK pk, String password) {
        this.pk = pk;
        this.password = password;
    }

    public abstract String getProperties();

    public abstract Executable getExecutor();

    public DatabasePK getPk() {
        return pk;
    }

    public String getDbUrl() {
        return pk.getDbUrl();
    }

    public String getName() {
        return DataSourceUtil.getDatabaseName(getDbUrl());
    }

    public String getUserName() {
        return pk.getUserName();
    }

    public String getPassword() {
        return password;
    }


}
