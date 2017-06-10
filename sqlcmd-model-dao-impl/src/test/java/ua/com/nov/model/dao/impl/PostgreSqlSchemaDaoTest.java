package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PostgreSqlSchemaDaoTest extends AbstractSchemaDaoTest {
    private static AbstractDatabaseDaoTest DATABASE_DAO_TEST = new PostgreSqlDatabaseDaoTest();

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException, DaoBusinessLogicException {
        PostgreSqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        DataSource dataSource = new SingleConnectionDataSource(testDb, "postgres", "postgres");
        DAO.setDataSource(dataSource);
        createTestData(null, null);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException, DaoSystemException {
        AbstractSchemaDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }

}
