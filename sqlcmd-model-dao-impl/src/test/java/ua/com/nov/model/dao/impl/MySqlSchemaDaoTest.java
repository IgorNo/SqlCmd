package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.MySqlDb;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class MySqlSchemaDaoTest extends AbstractSchemaDaoTest {
    private static AbstractDatabaseDaoTest DATABASE_DAO_TEST = new MySqlDatabaseDaoTest();

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException {
        MySqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        dataSource = new SingleConnectionDataSource(testDb, "root", "root");
        createTestData(null, null);
    }

    @Test
    @Override
    public void testReadSchema() throws DaoSystemException {
        Database db = new MySqlDb(testDb.getDbUrl(), "tmp_schema");
        Database result = new DatabaseDao().setDataSource(dataSource).read(db.getId());
        assertTrue(result.equals(db));
    }

    @Test
    @Override
    public void testReadAllSchemas() throws DaoSystemException {
    }

    @Test(expected = DaoSystemException.class)
    @Override
    public void testRenameSchema() throws DaoSystemException {
        super.testRenameSchema();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractSchemaDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }
}
