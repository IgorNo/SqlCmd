package ua.com.nov.model;

import ua.com.nov.model.entity.Database;

public class PostgreSqlLocalDataSource extends AbstractDataSource {

    public static String DB_URL = "jdbc:postgresql://localhost:5432/";

    public PostgreSqlLocalDataSource(Database database) {
        super(database);
    }

    @Override
    protected String getUrl() {
        return DB_URL;
    }
}
