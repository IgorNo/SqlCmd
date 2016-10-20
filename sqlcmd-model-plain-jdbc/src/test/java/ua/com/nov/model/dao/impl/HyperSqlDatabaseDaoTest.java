package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import ua.com.nov.model.SingleConnectionDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Database;
import ua.com.nov.model.util.DataSourceUrl;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

public class HyperSqlDatabaseDaoTest extends AbstractDataBaseDaoTest {
    public static final Database TEST_DATABASE = new Database("tmp", "root", "root");

    public static final AbstractDao<String, Database> DAO = new HyperSqlDatabaseDao();

    public static final DataSource DATA_SOURCE =
            new SingleConnectionDataSource(DataSourceUrl.HYPER_SQL, new Database("sys", "root", "root"));

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
        return DataSourceUrl.HYPER_SQL;
    }

    @Override
    @Test(expected = AssertionError.class)
    public void testDeleteDataBase() throws SQLException {
        super.testDeleteDataBase();
    }

    @After
    public void tearDown() throws SQLException{

    }

    @AfterClass
    public static void tearDownClass() throws SQLException{
        DATA_SOURCE.getConnection().close();
    }


}
