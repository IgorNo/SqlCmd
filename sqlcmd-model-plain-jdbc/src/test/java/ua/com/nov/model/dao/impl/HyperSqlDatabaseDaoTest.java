package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import ua.com.nov.model.SingleConnectionDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Database;
import ua.com.nov.model.util.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class HyperSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    public static final String URL = DataSourceUtil.HYPER_SQL_MEMORY_URL;

    public static final DataSource DATA_SOURCE =
            new SingleConnectionDataSource(new Database(URL + "sys", "root", "root"));

    public static final Database TEST_DATABASE = new Database(URL + "tmp", "root", "root");

    public static final AbstractDao<String, Database> DAO = new HyperSqlDatabaseDao();

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
    @Test(expected = AssertionError.class)
    public void testDeleteDataBase() throws SQLException {
        super.testDeleteDataBase();
    }

    @After
    public void tearDown() throws SQLException{ /*NOP*/ }

    @AfterClass
    public static void tearDownClass() throws SQLException{
        DATA_SOURCE.getConnection().close();
    }
}
