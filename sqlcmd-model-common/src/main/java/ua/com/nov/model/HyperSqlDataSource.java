package ua.com.nov.model;

import ua.com.nov.model.entity.Database;

public class HyperSqlDataSource extends AbstractDataSource {

    public static String DB_URL = "jdbc:hsqldb:file:lightdb/";

    public HyperSqlDataSource(Database database) {
        super(database);
    }

    @Override
    protected String getUrl() {
        return DB_URL;
    }
}
