package ua.com.nov.model.dao.impl;

import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Database;

import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseDao extends AbstractDao<String, Database> {

    public static final String CREATE_DB_SQL = "CREATE DATABASE ";
    public static final String DROP_DB_SQL = "DROP DATABASE ";

    protected DatabaseDao() {
    }

    @Override
    public void create(Database database) throws SQLException {
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(CREATE_DB_SQL + database.getName() + " " + database.getProperties());
        statement.close();
    }

    @Override
    public void delete(Database database) throws SQLException {
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(DROP_DB_SQL + database.getName());
        statement.close();
    }

    @Override
    public int count() throws SQLException {
        return readAll().size();
    }
}
