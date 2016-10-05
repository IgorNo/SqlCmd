package ua.com.nov.model;

public class HyperSqlDataSource extends BaseDataSource {

    public static String DB_URL = "jdbc:hsqldb:file:lightdb/";

    public HyperSqlDataSource(String dbName, String userName, String password) {
        super(dbName, userName, password);
    }

    @Override
    protected String getUrl() {
        return DB_URL;
    }
}
