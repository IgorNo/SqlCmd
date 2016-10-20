package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.SingleConnectionDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Database;
import ua.com.nov.model.util.DataSourceUrl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertTrue;

public class MySqlDatabaseDaoTest extends AbstractDataBaseDaoTest{
    public static final DataSource DATA_SOURCE =
            new SingleConnectionDataSource(DataSourceUrl.MY_SQL_LOCAL, new Database("sys", "root", "root"));
    public static final AbstractDao<String, Database> DAO = new MySqlDatabaseDao();
    public static final Database TEST_DATABASE = new Database("tmp", "root", "root");

    @Override
    public Database getTestDatabase() {
        return TEST_DATABASE;
    }

    @Override
    public DataSource getDataSource() throws SQLException {
        return DATA_SOURCE;
    }

    @Override
    public AbstractDao<String, Database> getDao() {
        return DAO;
    }

    @Override
    public String getDbUrl() {
        return DataSourceUrl.MY_SQL_LOCAL;
    }

    @AfterClass
    public static void tearDownClass() throws SQLException{
        DATA_SOURCE.getConnection().close();
    }
}
