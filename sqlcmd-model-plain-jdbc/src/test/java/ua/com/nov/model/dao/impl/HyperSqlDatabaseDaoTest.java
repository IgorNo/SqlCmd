package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.HyperSqlDb;

import java.sql.SQLException;

import static ua.com.nov.model.util.DbUtil.HYPER_SQL_MEMORY_URL;
import static ua.com.nov.model.util.DbUtil.HYPER_SQL_MEM_SYSTEM_DB;


public class HyperSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {

    public static final Database TEST_DATABASE = new HyperSqlDb(HYPER_SQL_MEMORY_URL, "tmp");

    @Override
    protected String getPassword() {
        return null;
    }

    @Override
    protected String getUserName() {
        return null;
    }

    @Override
    public Database getTestDatabase() {
        return TEST_DATABASE;
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        dataSource = new SingleConnectionDataSource(HYPER_SQL_MEM_SYSTEM_DB, null, null);
    }

    @Override
    @Before
    public void setUp() throws SQLException {
        DAO.setDataSource(dataSource);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteDataBase() throws SQLException {
        super.testDeleteDataBase();
    }

    @After
    public void tearDown() throws SQLException{ /*NOP*/ }
}
