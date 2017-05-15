package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.MySqlDb;
import ua.com.nov.model.util.DbUtil;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static ua.com.nov.model.util.DbUtil.MY_SQL_LOCAL_URL;

public class MySqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    public static final MySqlDb.Options OPTIONS = new MySqlDb.Options.Builder()
            .characterSet("utf8").collate("utf8_general_ci").build();
    public static final Database TEST_DATABASE = new MySqlDb(MY_SQL_LOCAL_URL, "tmp", OPTIONS);

    @Override
    public Database getTestDatabase() {
        return TEST_DATABASE;
    }

    @Override
    protected String getPassword() {
        return "root";
    }

    @Override
    protected String getUserName() {
        return "root";
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        dataSource = new SingleConnectionDataSource(new MySqlDb(DbUtil.MY_SQL_LOCAL_URL, ""),
                "root", "root");
    }

    @Test
    public void testRead() throws DaoSystemException {
        MySqlDb.Options options = (MySqlDb.Options) readDatabase().getOptions();
        assertTrue(OPTIONS.getCollate().equalsIgnoreCase(options.getCollate()));
        assertTrue(OPTIONS.getCharacterSet().equalsIgnoreCase(options.getCharacterSet()));
    }

    @Test
    public void testReadAll() throws DaoSystemException {
        List<Database> databases = DAO.readAll(new MySqlDb(MY_SQL_LOCAL_URL, ""));
        assertTrue(databases.size() > 1);
        assertTrue(databases.contains(TEST_DATABASE));
    }

    @Test
    public void testUpdateDatabase() throws DaoSystemException {
        MySqlDb updateDb = new MySqlDb(MY_SQL_LOCAL_URL, TEST_DATABASE.getName(),
                new MySqlDb.Options.Builder().characterSet("cp1251").collate("cp1251_general_ci").build());
        DAO.update(updateDb);
        Database readDb = DAO.read(TEST_DATABASE.getId());
        MySqlDb.Options options = (MySqlDb.Options) readDb.getOptions();
        assertTrue(options.getCharacterSet().equalsIgnoreCase(updateDb.getOptions().getOption("CHARACTER SET")));
        assertTrue(options.getCollate().equalsIgnoreCase(updateDb.getOptions().getOption("COLLATE")));
    }

}
