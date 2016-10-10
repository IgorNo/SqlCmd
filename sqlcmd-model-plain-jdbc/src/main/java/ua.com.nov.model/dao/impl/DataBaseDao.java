package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.BaseDao;
import ua.com.nov.model.entity.DataBase;

import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseDao extends BaseDao<String, DataBase> {

    public static final String CREATE_DB_SQL = "CREATE DATABASE ";
    public static final String DROP_DB_SQL = "DROP DATABASE IF EXISTS ";

    @Override
    public void create(DataBase dataBase) {
        try (Statement statement = dataSource.getConnection().createStatement()) {
            statement.executeUpdate(CREATE_DB_SQL + dataBase.getDbName() + " " + dataBase.getProperties());
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e.getCause());
        }
    }

    @Override
    public void delete(DataBase dataBase) {
        try (Statement statement = dataSource.getConnection().createStatement()) {
            statement.executeUpdate(DROP_DB_SQL + dataBase.getDbName() + " " + dataBase.getProperties());
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e.getCause());
        }
    }

}
