package ua.com.nov.model.entity;

import ua.com.nov.model.util.DataSourceUtil;

public class Database {
    private final String dbUrl;
    private String userName;
    private String password;
    private String properties = "";

    public Database(String dbUrl) {
        this(dbUrl, null, null);
    }

    public Database(String dbUrl, String userName, String password) {
        if (dbUrl == null || "".equals(dbUrl)) throw new IllegalArgumentException();
        this.dbUrl = dbUrl;
        this.userName = userName;
        this.password = password;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getName() {
        return DataSourceUtil.getDatabaseName(dbUrl);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getProperties() {
        return properties;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Database database = (Database) o;

        return dbUrl.equals(database.dbUrl);
    }

    @Override
    public int hashCode() {
        return dbUrl.hashCode();
    }
}
