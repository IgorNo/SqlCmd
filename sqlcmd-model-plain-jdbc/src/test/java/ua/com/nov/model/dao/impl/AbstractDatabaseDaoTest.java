package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.nov.model.datasource.MultiConnectionDataSource;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertTrue;

public abstract class AbstractDatabaseDaoTest {
    private static final String DROP_DB_IF_EXISTS_SQL = "DROP DATABASE IF EXISTS ";

    protected abstract DataSource getDataSource() throws SQLException;

    protected abstract AbstractDao<DatabaseID, Database, Object> getDao();

    public abstract Database getTestDatabase();

    @Before
    public void setUp() throws SQLException {
        tearDown();
        getDao().setDataSource(getDataSource());
        getDao().create(getTestDatabase());
    }

    @Test
    public void testCreateDataBase() throws SQLException {
        DataSource tmpDataSource = new MultiConnectionDataSource(getTestDatabase());
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn != null);
        conn.close();
    }

    @Test(expected = SQLException.class)
    public void testDeleteDataBase() throws SQLException {
        getDao().delete(getTestDatabase());
        DataSource tmpDataSource = new SingleConnectionDataSource(getTestDatabase());
        Connection conn = tmpDataSource.getConnection();
        assertTrue(conn == null);
        conn.close();
    }

    @After
    public void tearDown() throws SQLException{
        Statement statement = getDataSource().getConnection().createStatement();
        statement.executeUpdate(DROP_DB_IF_EXISTS_SQL + getTestDatabase().getName());
        statement.close();
    }
}
