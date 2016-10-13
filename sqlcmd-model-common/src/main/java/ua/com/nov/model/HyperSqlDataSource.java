package ua.com.nov.model;

import ua.com.nov.model.entity.DataBase;

public class HyperSqlDataSource extends AbstractDataSource {

    public static String DB_URL = "jdbc:hsqldb:file:lightdb/";

    public HyperSqlDataSource(DataBase dataBase) {
        super(dataBase);
    }

    @Override
    protected String getUrl() {
        return DB_URL;
    }
}
