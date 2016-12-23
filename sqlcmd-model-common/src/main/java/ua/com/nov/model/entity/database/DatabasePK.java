package ua.com.nov.model.entity.database;

public class DatabasePK {
    private String dbUrl;
    private String userName;

    public DatabasePK(String dbUrl, String userName) {
        if (dbUrl == null || "".equals(dbUrl)) throw new IllegalArgumentException();
        if (userName == null || "".equals(userName)) throw new IllegalArgumentException();
        this.dbUrl = dbUrl;
        this.userName = userName;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabasePK that = (DatabasePK) o;

        if (!dbUrl.equals(that.dbUrl)) return false;
        return userName.equals(that.userName);

    }

    @Override
    public int hashCode() {
        int result = dbUrl.hashCode();
        result = 31 * result + userName.hashCode();
        return result;
    }
}
