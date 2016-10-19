package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.MySqlLocalDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertTrue;

public class MySqlDatabaseDaoTest {
    private static final String DROP_DB_SQL = "DROP DATABASE IF EXISTS ";

    public static final DataSource DATA_SOURCE = new MySqlLocalDataSource(new Database("sys", "root", "root"));
    public static final AbstractDao DAO = new MySqlDatabaseDao();
    public static final Database TEST_DATABASE = new Database("tmp", "root", "root");

    @Before
    public void setUp() throws SQLException {
        DAO.setDataSource(DATA_SOURCE);
        DAO.create(TEST_DATABASE);
    }

    @Test
    public void testCreateDataBase() throws SQLException {
        DataSource tmpDataSource = new MySqlLocalDataSource(TEST_DATABASE);
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn != null);
        conn.close();
    }

    @Test(expected = SQLException.class)
    public void testDeleteDataBase() throws SQLException {
        DAO.delete(TEST_DATABASE);
        DataSource tmpDataSource = new MySqlLocalDataSource(TEST_DATABASE);
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn == null);
        conn.close();
    }

    @After
    public void tearDown() throws SQLException{
        Statement statement = DATA_SOURCE.getConnection().createStatement();
        statement.executeUpdate(DROP_DB_SQL + TEST_DATABASE.getDbName());
        statement.close();
    }
}
