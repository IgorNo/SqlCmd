package ua.com.nov.model.dao.impl;

import org.hsqldb.HsqlException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.dao.BaseDao;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;
import ua.com.nov.model.entity.database.HyperSqlDb;
import ua.com.nov.model.util.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

public class HyperSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    public static final String URL = DbUtil.HYPER_SQL_MEMORY_URL;

    public static final DataSource DATA_SOURCE = DbUtil.HYPER_SQL_MEM_SYSTEM_DB;

    public static final Database TEST_DATABASE = new HyperSqlDb(URL + "tmp", "root", "root");

    @Override
    public Database getTestDatabase() {
        return TEST_DATABASE;
    }

    @Override
    public DataSource getDataSourceDB() {
        return DATA_SOURCE;
    }

    @Override
    @Before
    public void setUp() throws SQLException {
        DAO.setDataSource(getDataSource());
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteDataBase() throws SQLException {
        super.testDeleteDataBase();
    }

    @After
    public void tearDown() throws SQLException{ /*NOP*/ }
}
