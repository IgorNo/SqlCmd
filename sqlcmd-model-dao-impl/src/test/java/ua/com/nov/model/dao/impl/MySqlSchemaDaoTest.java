package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class MySqlSchemaDaoTest extends AbstractSchemaDaoTest {
    private static AbstractDatabaseDaoTest DATABASE_DAO_TEST = new MySqlDatabaseDaoTest();

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException {
        MySqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        DataSource dataSource = new SingleConnectionDataSource(testDb, "root", "root");
        DAO.setDataSource(dataSource);
        createTestData(null, null);
    }

    @Test
    @Override
    public void testReadSchema() throws DaoSystemException {
        Database db = new Database(testDb.getServer(), "tmp_schema");
        Database result = new DatabaseDao().setDataSource(DAO.getDataSource()).read(db.getId());
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
    public static void tearDownClass() throws SQLException, DaoSystemException {
        AbstractSchemaDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }
}
