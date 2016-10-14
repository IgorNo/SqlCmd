package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.DataBase;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DataBaseDao extends AbstractDao<String, DataBase> {

    public static final String CREATE_DB_SQL = "CREATE DATABASE ";
    public static final String DROP_DB_SQL = "DROP DATABASE ";

    protected DataBaseDao() {
    }

    @Override
    public void create(DataBase dataBase) throws SQLException {
        Statement statement = dataSource.getConnection().createStatement();
        statement.executeUpdate(CREATE_DB_SQL + dataBase.getDbName() + " " + dataBase.getProperties());
        statement.close();
    }

    @Override
    public void delete(DataBase dataBase) throws SQLException {
        Statement statement = dataSource.getConnection().createStatement();
        statement.executeUpdate(DROP_DB_SQL + dataBase.getDbName());
        statement.close();
    }

    @Override
    public int count() throws SQLException {
        return readAll().size();
    }
}
