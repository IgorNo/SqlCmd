package ua.com.nov.model;

public class PostgreSqlLocalDataSource extends BaseDataSource {

    public static String DB_URL = "jdbc:postgresql://localhost:5432/";

    public PostgreSqlLocalDataSource(String dbName, String userName, String password) {
        super(dbName, userName, password);
    }

    @Override
    protected String getUrl() {
        return DB_URL;
    }
}
