package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.PostgreSqlLocalDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PostgreSqlDatabaseDaoTest {

    private static final String DROP_DB_SQL = "DROP DATABASE IF EXISTS ";

    private static DataSource dataSource = new PostgreSqlLocalDataSource(new Database("postgres", "postgres", "postgres"));
    private static AbstractDao dao = new PostgreSqlDatabaseDao();
    private static Database tmpDatabase = new Database("tmp", "postgres", "postgres");

    @Before
    public void setUp() throws SQLException {
        dao.setDataSource(dataSource);
        dao.create(tmpDatabase);
    }

    @Test
    public void testCreateDataBase() throws SQLException {
        DataSource tmpDataSource = new PostgreSqlLocalDataSource(tmpDatabase);
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn != null);
        conn.close();
    }

    @Test(expected = SQLException.class)
    public void testDeleteDataBase() throws SQLException {
        dao.delete(tmpDatabase);
        DataSource tmpDataSource = new PostgreSqlLocalDataSource(tmpDatabase);
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn == null);
        conn.close();
    }

    @Test
    public void testReadAll() throws SQLException{
        List<Database> databaseList = dao.readAll();
        assertTrue(databaseList.contains(new Database("postgres", "postgres", "postgres")));
        assertTrue(databaseList.contains(tmpDatabase));
    }

    @Test
    public void testCount() throws SQLException {
        assertTrue(dao.count() > 0);
    }

    @After
    public void tearDown() throws SQLException{
        Statement statement = dataSource.getConnection().createStatement();
        statement.executeUpdate(DROP_DB_SQL + tmpDatabase.getDbName());
        statement.close();
    }
}
