package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;

import java.sql.SQLException;

public class PostgresSqlSchemaDaoTest extends AbstractSchemaDaoTest {
    private static AbstractDatabaseDaoTest DATABASE_DAO_TEST = new PostgreSqlDatabaseDaoTest();

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException, DaoBusinessLogicException {
        PostgreSqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        dataSource = new SingleConnectionDataSource(testDb, "postgres", "postgres");
        createTestData(null, null);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractSchemaDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }

}
