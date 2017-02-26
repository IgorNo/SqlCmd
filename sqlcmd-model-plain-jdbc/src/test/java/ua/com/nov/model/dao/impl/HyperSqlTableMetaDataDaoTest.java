package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.Before;
import ua.com.nov.model.entity.database.Database;

import java.sql.SQLException;

public class HyperSqlTableMetaDataDaoTest extends AbstractTableDaoTest {
    public static final AbstractDatabaseDaoTest DATABASE_DAO_TEST = new HyperSqlDatabaseDaoTest();

    @Override
    protected Database getTestDatabase() {
        return DATABASE_DAO_TEST.getTestDatabase();
    }

    @Before
    @Override
    public void setUp() throws SQLException {
        createTestData(null, "PUBLIC");
        super.setUp();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }
}
