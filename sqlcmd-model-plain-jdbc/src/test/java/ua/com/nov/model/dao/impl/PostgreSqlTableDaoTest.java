package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import ua.com.nov.model.entity.Database;
import ua.com.nov.model.entity.Table;
import ua.com.nov.model.entity.TablePK;

import java.sql.SQLException;

public class PostgreSqlTableDaoTest extends AbstractTableDaoTest {
    public static final AbstractDatabaseDaoTest DB_TEST = new PostgreSqlDatabaseDaoTest();

    @Override
    public Database getTestDatabase() {
        return DB_TEST.getTestDatabase();
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        createDataSource(DB_TEST);
    }

    @Before
    public void setUp() throws SQLException{
        createTestData("public");
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        DAO.getDataSource().getConnection().close();
        DB_TEST.tearDown();
    }
}
