package ua.com.nov.model.dao.impl;

import org.junit.Test;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.PostgresSqlDb;
import ua.com.nov.model.util.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PostgreSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    public static final String URL = DbUtil.POSTGRE_SQL_LOCAL_URL;

    public static final DataSource DATA_SOURCE = DbUtil.POSTGRE_SQL_LOCAL_SYSTEM_DB;

    public static final Database TEST_DATABASE = new PostgresSqlDb(URL + "tmp", "postgres", "postgres");

    @Override
    public Database getTestDatabase() {
        return TEST_DATABASE;
    }

    @Override
    public DataSource getDataSourceDB() {
        return DATA_SOURCE;
    }

    @Test
    public void testReadAll() throws SQLException {
        List<Database> databases = DAO.readAll(TEST_DATABASE.getId());
        assertTrue(databases.contains(TEST_DATABASE));
    }

}
