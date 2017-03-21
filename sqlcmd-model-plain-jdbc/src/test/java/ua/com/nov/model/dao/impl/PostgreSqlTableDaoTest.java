package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import ua.com.nov.model.entity.metadata.database.Database;

import java.sql.SQLException;

public class PostgreSqlTableDaoTest extends AbstractTableDaoTest {
    public static final AbstractDatabaseDaoTest DATABASE_DAO_TEST = new PostgreSqlDatabaseDaoTest();

    @Override
    protected Database getTestDatabase() {
        return DATABASE_DAO_TEST.getTestDatabase();
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        DATABASE_DAO_TEST.setUp();
    }

    @Before
    @Override
    public void setUp() throws SQLException{
        createTestData(null, "public", "serial");
        super.setUp();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }
}
