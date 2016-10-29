package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.Test;
import ua.com.nov.model.SingleConnectionDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Database;
import ua.com.nov.model.util.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class PostgreSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    public static final String URL = DataSourceUtil.POSTGRE_SQL_LOCAL_URL;

    public static final DataSource DATA_SOURCE =
            new SingleConnectionDataSource(new Database(URL + "postgres", "postgres", "postgres"));

    public static final AbstractDao<String, Database> DAO = new PostgreSqlDatabaseDao();

    public static final Database TEST_DATABASE = new Database(URL + "tmp", "postgres", "postgres");

    @Override
    public Database getTestDatabase() {
        return TEST_DATABASE;
    }

    @Override
    public DataSource getDataSource() throws SQLException {
        return DATA_SOURCE;
    }

    @Override
    public AbstractDao<String, Database> getDao() {
        return DAO;
    }

    @Test
    public void testReadAll() throws SQLException {
        Map<String, Database> databases = DAO.readAll();
        assertTrue(databases.containsKey(URL + "tmp"));
    }

    @AfterClass
    public static void tearDownClass() throws SQLException{
        DATA_SOURCE.getConnection().close();
    }
}
