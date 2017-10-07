package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.server.HsqldbServer;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.util.DbConstants;

import javax.sql.DataSource;
import java.sql.SQLException;

public class HyperSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    private static final Server SERVER = new HsqldbServer(DbConstants.HYPER_SQL_FILE_URL);
    public static final Database TEST_DB = new Database(SERVER, "tmp");

    @BeforeClass
    public static void setUpClass() throws SQLException {
        DataSource dataSource = new SingleConnectionDataSource(SERVER.getName(), "",
                "root", "root");
        SERVER.init(dataSource.getConnection());
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
    public void setUp() throws SQLException, DAOSystemException {
        tearDown();
    }

    @Test
    public void testRead() throws DAOSystemException {
        readDatabase();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteDataBase() throws SQLException, DAOSystemException {
        super.testDeleteDataBase();
    }

    @After
    public void tearDown() throws DAOSystemException { /*NOP*/ }
}
