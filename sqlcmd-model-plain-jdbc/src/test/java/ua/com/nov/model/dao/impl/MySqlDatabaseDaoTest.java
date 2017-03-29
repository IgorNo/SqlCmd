package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.MySqlDb;
import ua.com.nov.model.util.DbUtil;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static ua.com.nov.model.util.DbUtil.MY_SQL_LOCAL_URL;

public class MySqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    public static final Database TEST_DATABASE =
            new MySqlDb(MY_SQL_LOCAL_URL, "tmp", new MySqlDb.Options("utf8", "utf8_general_ci"));

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
    public void testReadAll() throws SQLException {
        List<Database> databases = DAO.readAll(new MySqlDb(MY_SQL_LOCAL_URL, ""));
        assertTrue(databases.contains(TEST_DATABASE));
    }

    @Test
    public void testUpdateDatabase() throws SQLException {
        DAO.update(new MySqlDb(MY_SQL_LOCAL_URL, TEST_DATABASE.getName(),
                new MySqlDb.Options("cp1251", "cp1251_general_ci")));
    }

}
