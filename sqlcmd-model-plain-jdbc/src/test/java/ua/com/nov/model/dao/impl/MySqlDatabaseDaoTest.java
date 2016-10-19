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

    private static DataSource dataSource = new MySqlLocalDataSource(new Database("sys", "root", "root"));
    private static AbstractDao dao = new MySqlDatabaseDao();
    private static Database tmpDatabase = new Database("tmp", "root", "root");

    @Before
    public void setUp() throws SQLException {
        dao.setDataSource(dataSource);
        dao.create(tmpDatabase);
    }

    @Test
    public void testCreateDataBase() throws SQLException {
        DataSource tmpDataSource = new MySqlLocalDataSource(tmpDatabase);
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn != null);
        conn.close();
    }

    @Test(expected = SQLException.class)
    public void testDeleteDataBase() throws SQLException {
        dao.delete(tmpDatabase);
        DataSource tmpDataSource = new MySqlLocalDataSource(tmpDatabase);
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn == null);
        conn.close();
    }

    @After
    public void tearDown() throws SQLException{
        Statement statement = dataSource.getConnection().createStatement();
        statement.executeUpdate(DROP_DB_SQL + tmpDatabase.getDbName());
        statement.close();
    }
}
