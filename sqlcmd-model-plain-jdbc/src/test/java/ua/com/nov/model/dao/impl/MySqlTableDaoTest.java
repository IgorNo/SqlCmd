package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class MySqlTableDaoTest extends AbstractTableDaoTest {
    public static final AbstractDatabaseDaoTest DATABASE_DAO_TEST = new MySqlDatabaseDaoTest();

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
    public void setUp() throws SQLException {
        createTestData(getTestDatabase().getName(), null, "INT");
        super.setUp();
    }

    @Override
    @Test (expected = UnsupportedOperationException.class)
    public void testRenamePrimaryKey() throws SQLException {
        PrimaryKey testPk = customers.getPrimaryKey();
        testPk.setNewName("test");
        PRIMARY_KEY_DAO.update(testPk);
        assertTrue(false);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testRenameForeignKey() throws SQLException {
        ForeignKey testPk = orders.getForeignKeyList().get(0);
        testPk.setNewName("test");
        FOREIGN_KEY_DAO.update(testPk);
        assertTrue(false);
    }


    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }
}
