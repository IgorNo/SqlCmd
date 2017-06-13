package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.server.HyperSqlServer;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.util.DbUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

public class HyperSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    private static final Server SERVER = new HyperSqlServer(DbUtils.HYPER_SQL_FILE_URL);
    public static final Database TEST_DB = new Database(SERVER, "tmp");

    @BeforeClass
    public static void setUpClass() throws SQLException {
        DataSource dataSource = new SingleConnectionDataSource(new Database(SERVER, ""),
                "root", "root");
        DAO.setDataSource(dataSource);
    }

    @Override
    protected String getPassword() {
        return "root";
    }

    @Override
    protected String getUserName() {
        return "root";
    }

    @Override
    public Database getTestDatabase() {
        return TEST_DB;
    }

    @Before
    public void setUp() throws SQLException, DaoSystemException {
        tearDown();
    }

    @Test
    public void testRead() throws DaoSystemException {
        readDatabase();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteDataBase() throws SQLException, DaoSystemException {
        super.testDeleteDataBase();
    }

    @After
    public void tearDown() throws DaoSystemException { /*NOP*/ }
}
