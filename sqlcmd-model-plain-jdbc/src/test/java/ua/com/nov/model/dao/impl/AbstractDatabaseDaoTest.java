package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.dao.BaseDao;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertTrue;

public abstract class AbstractDatabaseDaoTest {
    private static final String DROP_DB_IF_EXISTS_SQL = "DROP DATABASE IF EXISTS ";

    public static final BaseDao<DatabaseID, Database, Database> DAO = new DatabaseDao();

    private static DataSource dataSource;

    public abstract DataSource getDataSourceDB();
    public abstract Database getTestDatabase();

    public DataSource getDataSource() throws SQLException {
        if (dataSource == null) dataSource =  new SingleConnectionDataSource(getDataSourceDB());
        return dataSource;
    }

    @Before
    public void setUp() throws SQLException {
        tearDown();
        DAO.setDataSource(getDataSource());
        DAO.create(getTestDatabase());
    }

    @Test
    public void testCreateDataBase() throws SQLException {
        Connection conn = getTestDatabase().getConnection();
        assertTrue(conn != null);
        conn.close();
    }

    @Test(expected = SQLException.class)
    public void testDeleteDataBase() throws SQLException {
        DAO.delete(getTestDatabase());
        Connection conn = getTestDatabase().getConnection();
        assertTrue(conn == null);
        conn.close();
    }

    @After
    public void tearDown() throws SQLException{
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(DROP_DB_IF_EXISTS_SQL + getTestDatabase().getName());
        statement.close();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException{
        dataSource.getConnection().close();
    }
}
