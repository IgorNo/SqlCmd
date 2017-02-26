package ua.com.nov.model.entity.database;

public class DatabaseId {
    private final String dbUrl;
    private final String userName;

    public DatabaseId(String dbUrl, String userName) {
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

        DatabaseId that = (DatabaseId) o;

        if (!dbUrl.equalsIgnoreCase(that.dbUrl)) return false;
        return userName.equals(that.userName);

    }

    @Override
    public int hashCode() {
        int result = dbUrl.toLowerCase().hashCode();
        result = 31 * result + userName.toLowerCase().hashCode();
        return result;
    }
}
