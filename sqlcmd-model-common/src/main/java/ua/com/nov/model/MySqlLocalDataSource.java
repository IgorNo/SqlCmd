package ua.com.nov.model;

import ua.com.nov.model.entity.DataBase;

public class MySqlLocalDataSource extends AbstractDataSource {

    public static String DB_URL = "jdbc:mysql://localhost:3306/";

    public MySqlLocalDataSource(DataBase dataBase) {
       super(dataBase);
    }

    @Override
    protected String getUrl() {
        return DB_URL;
    }
}
