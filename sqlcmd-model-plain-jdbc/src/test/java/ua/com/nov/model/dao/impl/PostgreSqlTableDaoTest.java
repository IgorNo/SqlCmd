package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import ua.com.nov.model.entity.database.Database;

import java.sql.SQLException;

public class PostgreSqlTableDaoTest extends AbstractTableDaoTest {
    public static final AbstractDatabaseDaoTest DB_TEST = new PostgreSqlDatabaseDaoTest();

    @Override
    public Database getTestDatabase() {
        return DB_TEST.getTestDatabase();
    }

    @Before
    public void setUp() throws SQLException{
        createTestData("public");
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractTableDaoTest.tearDownClass();
        DB_TEST.tearDown();
    }
}
