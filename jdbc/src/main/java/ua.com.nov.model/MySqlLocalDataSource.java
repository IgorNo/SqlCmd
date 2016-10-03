package ua.com.nov.model;

public class MySqlLocalDataSource extends BaseDataSource {

    public static String DB_URL = "jdbc:mysql://localhost:3306/";

    public MySqlLocalDataSource(String dbName, String userName, String password) {
       super(dbName, userName, password);
    }

    @Override
    protected String getUrl() {
        return DB_URL;
    }
}
