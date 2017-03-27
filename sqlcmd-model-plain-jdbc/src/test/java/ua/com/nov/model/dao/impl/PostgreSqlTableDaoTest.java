package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import ua.com.nov.model.datasource.SingleConnectionDataSource;

import java.sql.SQLException;

public class PostgreSqlTableDaoTest extends AbstractTableDaoTest {
    public static final AbstractDatabaseDaoTest DATABASE_DAO_TEST = new PostgreSqlDatabaseDaoTest();

    @BeforeClass
    public static void setUpClass() throws SQLException {
        PostgreSqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        dataSource = new SingleConnectionDataSource(testDb, "postgres", "postgres");
        createTestData(null, "public", "serial");
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }
}
