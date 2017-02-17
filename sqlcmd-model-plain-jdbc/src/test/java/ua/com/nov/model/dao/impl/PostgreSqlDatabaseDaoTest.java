package ua.com.nov.model.dao.impl;

import org.junit.Test;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.database.DatabaseID;
import ua.com.nov.model.entity.database.PostgreSqlDb;
import ua.com.nov.model.util.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class PostgreSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    public static final String URL = DbUtil.POSTGRE_SQL_LOCAL_URL;

    public static final DataSource DATA_SOURCE = DbUtil.POSTGRE_SQL_LOCAL_SYSTEM_DB;

    public static final AbstractDao<DatabaseID, Database, Object> DAO = new PostgreSqlDatabaseDao();

    public static final Database TEST_DATABASE = new PostgreSqlDb(URL + "tmp", "postgres", "postgres");

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

    @Test
    public void testReadAll() throws SQLException {
        Map<DatabaseID, Database> databases = DAO.readAll();
        DatabaseID databaseID = new DatabaseID(URL + "tmp", "postgres");
        assertTrue(databases.containsKey(databaseID));
    }

}
