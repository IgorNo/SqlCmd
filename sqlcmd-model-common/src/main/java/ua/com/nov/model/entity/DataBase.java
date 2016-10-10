package ua.com.nov.model.entity;

import javax.sql.DataSource;

public class DataBase {

    private String dbName;
    private String userName;
    private String password;
    private String properties = "";

    public DataBase(String dbName, String userName, String password) {
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
    }

    public String getDbName() {
        return dbName;
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

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
