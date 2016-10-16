package ua.com.nov.model.entity;

public class DataBase {
    private String dbName;
    private String userName;
    private String password;
    private String properties = "";

    public DataBase(String dbName) {
        this(dbName, null, null);
    }

    public DataBase(String dbName, String userName, String password) {
        if (dbName == null || "".equals(dbName)) throw new IllegalArgumentException();
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

        DataBase dataBase = (DataBase) o;

        return dbName.equals(dataBase.dbName);
    }

    @Override
    public int hashCode() {
        return dbName.hashCode();
    }
}
