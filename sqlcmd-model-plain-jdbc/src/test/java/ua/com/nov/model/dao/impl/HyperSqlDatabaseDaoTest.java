package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Test;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;
import ua.com.nov.model.entity.database.HyperSqlDb;
import ua.com.nov.model.util.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class HyperSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    public static final String URL = DbUtil.HYPER_SQL_MEMORY_URL;

    public static final DataSource DATA_SOURCE = DbUtil.HYPER_SQL_MEM_SYSTEM_DB;

    public static final AbstractDao<DatabaseID, Database, Object> DAO = new HyperSqlDatabaseDao();

    public static final Database TEST_DATABASE = new HyperSqlDb(URL + "tmp", "root", "root");

    @Override
    public Database getTestDatabase() {
        return TEST_DATABASE;
    }

    @Override
    public DataSource getDataSourceDB() {
        return DATA_SOURCE;
    }

    @Override
    public AbstractDao<DatabaseID, Database, Object> getDao() {
        return DAO;
    }

    @Override
    @Test(expected = AssertionError.class)
    public void testDeleteDataBase() throws SQLException {
        super.testDeleteDataBase();
    }

    @After
    public void tearDown() throws SQLException{ /*NOP*/ }
}
