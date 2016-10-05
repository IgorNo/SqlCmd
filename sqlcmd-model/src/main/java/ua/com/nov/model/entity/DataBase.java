package ua.com.nov.model.entity;

import javax.sql.DataSource;

public class DataBase {

    private String dbName;
    private String userName;
    private String password;

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

}
