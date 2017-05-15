package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.dao.Dao;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.Database.Id;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertTrue;

public abstract class AbstractDatabaseDaoTest {
    private static final String DROP_DB_IF_EXISTS_SQL = "DROP DATABASE IF EXISTS ";

    public static final Dao<Id, Database, Database> DAO = new DatabaseDao();

    protected static DataSource dataSource;

    public abstract Database getTestDatabase();

    @Before
    public void setUp() throws SQLException, DaoSystemException {
        tearDown();
        DAO.setDataSource(dataSource);
        DAO.create(getTestDatabase());
    }

    @Test
    public void testCreateDataBase() throws SQLException {
        Connection conn = getTestDatabase().getConnection(getUserName(), getPassword());
        assertTrue(conn != null);
        conn.close();
    }

    protected abstract String getPassword();

    protected abstract String getUserName();

    public Database readDatabase() throws DaoSystemException {
        Database db = DAO.read(getTestDatabase().getId());
        assertTrue(getTestDatabase().equals(db));
        return db;
    }

        @Test(expected = SQLException.class)
    public void testDeleteDataBase() throws SQLException, DaoSystemException {
        DAO.delete(getTestDatabase());
        Connection conn = getTestDatabase().getConnection(getUserName(), getPassword());
        assertTrue(false);
        conn.close();
    }

    @After
    public void tearDown() throws SQLException{
        Statement statement = dataSource.getConnection().createStatement();
        statement.executeUpdate(DROP_DB_IF_EXISTS_SQL + getTestDatabase().getName());
        statement.close();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException{
        dataSource.getConnection().close();
    }
}
