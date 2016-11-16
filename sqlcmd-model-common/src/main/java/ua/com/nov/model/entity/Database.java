package ua.com.nov.model.entity;

import ua.com.nov.model.util.DataSourceUtil;

public class Database {
    private DatabasePK pk;
    private String password;
    private String properties = "";

    public Database(DatabasePK pk) {
        this(pk, null);
    }

    public Database(DatabasePK pk, String password) {
        this.pk = pk;
        this.password = password;
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

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

}
