package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.entity.metadata.database.Database;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public abstract class AbstractDatabaseDaoTest {
    protected static final DatabaseDao DAO = new DatabaseDao();

    public abstract Database getTestDatabase();

    @Before
    public void setUp() throws SQLException, DaoSystemException {
        tearDown();
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
    public void tearDown() throws DaoSystemException {
        DAO.deleteIfExist(getTestDatabase());
    }

    @AfterClass
    public static void tearDownClass() throws SQLException{
        DAO.getDataSource().getConnection().close();
    }
}
