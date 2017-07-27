package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.MappingSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.MySqlDbOptions;
import ua.com.nov.model.entity.metadata.server.MySqlServer;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.util.DbUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class MySqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    private static Server server = new MySqlServer(DbUtils.MY_SQL_LOCAL_URL);

    public static final MySqlDbOptions OPTIONS = new MySqlDbOptions.Builder()
            .characterSet("utf8").collate("utf8_general_ci").build();
    public static final Database TEST_DATABASE = new Database(server, "tmp", OPTIONS);

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
        DataSource dataSource = new SingleConnectionDataSource(new Database(server, ""), "root", "root");
        DAO.setDataSource(dataSource);
    }

    @Test
    public void testRead() throws MappingSystemException {
        MySqlDbOptions options = (MySqlDbOptions) readDatabase().getOptions();
        assertTrue(OPTIONS.getCollate().equalsIgnoreCase(options.getCollate()));
        assertTrue(OPTIONS.getCharacterSet().equalsIgnoreCase(options.getCharacterSet()));
    }

    @Test
    public void testReadAll() throws MappingSystemException {
        List<Database> databases = DAO.readAll(server.getId());
        assertTrue(databases.size() > 1);
        assertTrue(databases.contains(TEST_DATABASE));
    }

    @Test
    public void testUpdateDatabase() throws MappingSystemException {
        Database updateDb = new Database(server, TEST_DATABASE.getName(),
                new MySqlDbOptions.Builder().characterSet("cp1251").collate("cp1251_general_ci").build());
        DAO.update(updateDb);
        Database readDb = DAO.read(TEST_DATABASE.getId());
        MySqlDbOptions options = (MySqlDbOptions) readDb.getOptions();
        assertTrue(options.getCharacterSet().equalsIgnoreCase(updateDb.getOptions().getOption("CHARACTER SET")));
        assertTrue(options.getCollate().equalsIgnoreCase(updateDb.getOptions().getOption("COLLATE")));
    }

}
