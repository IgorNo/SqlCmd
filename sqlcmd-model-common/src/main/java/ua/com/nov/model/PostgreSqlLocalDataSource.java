package ua.com.nov.model;

import ua.com.nov.model.entity.DataBase;

public class PostgreSqlLocalDataSource extends BaseDataSource {

    public static String DB_URL = "jdbc:postgresql://localhost:5432/";

    public PostgreSqlLocalDataSource(DataBase dataBase) {
        super(dataBase);
    }

    @Override
    protected String getUrl() {
        return DB_URL;
    }
}
