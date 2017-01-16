package ua.com.nov.model.entity.database;

public class DatabaseID {
    private String dbUrl;
    private String userName;

    public DatabaseID(String dbUrl, String userName) {
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

        DatabaseID that = (DatabaseID) o;

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
