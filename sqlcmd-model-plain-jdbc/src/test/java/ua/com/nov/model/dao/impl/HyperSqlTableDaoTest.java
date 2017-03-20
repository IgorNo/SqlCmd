package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class HyperSqlTableDaoTest extends AbstractTableDaoTest {
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

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testRenamePrimaryKey() throws SQLException {
        PrimaryKey testPk = customers.getPrimaryKey();
        testPk.setNewName("test");
        PRIMARY_KEY_DAO.update(testPk);
        assertTrue(false);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }
}
