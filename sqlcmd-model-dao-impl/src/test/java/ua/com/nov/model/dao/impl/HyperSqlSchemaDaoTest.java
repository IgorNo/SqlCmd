package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;

import java.sql.SQLException;

public class HyperSqlSchemaDaoTest extends AbstractSchemaDaoTest{
    private static AbstractDatabaseDaoTest DATABASE_DAO_TEST = new HyperSqlDatabaseDaoTest();

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException, DaoBusinessLogicException {
        HyperSqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        dataSource = new SingleConnectionDataSource(testDb, "root", "root");
        createTestData("PUBLIC", null);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractSchemaDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }

}
