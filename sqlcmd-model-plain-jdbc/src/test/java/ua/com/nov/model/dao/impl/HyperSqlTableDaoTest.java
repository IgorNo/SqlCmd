package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class HyperSqlTableDaoTest extends AbstractTableDaoTest {
    public static final AbstractDatabaseDaoTest DATABASE_DAO_TEST = new HyperSqlDatabaseDaoTest();

    @BeforeClass
    public static void setUpClass() throws SQLException {
        HyperSqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        dataSource = new SingleConnectionDataSource(testDb, null, null);
        createTestData(null, "PUBLIC", "INTEGER");
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
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

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testRenameUniqueKey() throws SQLException {
        UniqueKey testUk = customers.getUniqueKeyList().get(0);
        testUk.setNewName("test");
        UNIQUE_KEY_DAO.update(testUk);
        assertTrue(false);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }
}
