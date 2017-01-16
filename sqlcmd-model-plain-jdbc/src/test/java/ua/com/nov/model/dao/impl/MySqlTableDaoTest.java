package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import ua.com.nov.model.entity.database.Database;

import java.sql.SQLException;

public class MySqlTableDaoTest extends AbstractTableDaoTest {
    public static final AbstractDatabaseDaoTest DB_TEST = new MySqlDatabaseDaoTest();

    @Override
    public Database getTestDatabase() {
        return DB_TEST.getTestDatabase();
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        createDataSource(DB_TEST);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        DB_TEST.tearDown();
    }

}
