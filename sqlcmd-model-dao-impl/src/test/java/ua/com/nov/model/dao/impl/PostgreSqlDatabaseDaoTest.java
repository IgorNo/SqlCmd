package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DAOSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.PostgreSqlDbOptions;
import ua.com.nov.model.entity.metadata.server.PostgresqlServer;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.util.DbConstants;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PostgreSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    private static Server server = new PostgresqlServer(DbConstants.POSTGRE_SQL_LOCAL_URL);

    public static final PostgreSqlDbOptions OPTIONS = new PostgreSqlDbOptions.Builder()
            .owner("postgres").encoding("UTF8")
            .lcCollate("Russian_Russia.1251").lcType("Russian_Russia.1251").tableSpace("pg_default")
            .connLimit(-1).allowConn(true).isTemplate(false).build();

    public static final Database TEST_DATABASE = new Database(server,"tmp", OPTIONS);

    @Override
    public Database getTestDatabase() {
        return TEST_DATABASE;
    }

    @Override
    protected String getPassword() {
        return "postgres";
    }

    @Override
    protected String getUserName() {
        return "postgres";
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        DataSource dataSource = new SingleConnectionDataSource(server.getName(),
                "", "postgres", "postgres");
        server.init(dataSource.getConnection());
        DAO.setDataSource(dataSource);
    }

    @Test
    public void testRead() throws DAOSystemException {
        Database db = DAO.read(TEST_DATABASE.getId());
        assertTrue(TEST_DATABASE.equals(db));
        PostgreSqlDbOptions options = (PostgreSqlDbOptions) db.getOptions();
        compareOptions(OPTIONS, options);
        assertTrue(OPTIONS.getEncoding().equalsIgnoreCase(options.getEncoding()));
        assertTrue(OPTIONS.getLcCollate().equalsIgnoreCase(options.getLcCollate()));
        assertTrue(OPTIONS.getLcType().equalsIgnoreCase(options.getLcType()));
    }

    private void compareOptions(PostgreSqlDbOptions options1, PostgreSqlDbOptions options2) {
        assertTrue(options1.getOwner().equalsIgnoreCase(options2.getOwner()));
        assertTrue(options1.getTableSpace().equalsIgnoreCase(options2.getTableSpace()));
        assertTrue(options1.getAllowConn().equals(options2.getAllowConn()));
        assertTrue(options1.isTemplate().equals(options2.isTemplate()));
        assertTrue(options1.getConnLimit().equals(options2.getConnLimit()));
    }


    @Test
    public void testReadAll() throws DAOSystemException {
        List<Database> databases = DAO.readAll(server.getId());
        assertTrue(databases.contains(TEST_DATABASE));
    }

    @Test
    public void testUpdateDatabase() throws DAOSystemException {
        PostgreSqlDbOptions uOptions = new PostgreSqlDbOptions.Builder()
                .owner("postgres").connLimit(100).allowConn(true)
                .tableSpace("pg_default").isTemplate(false)
                .build();
        Database updateDb = new Database(server, TEST_DATABASE.getName(),uOptions);
        DAO.update(updateDb);
        Database db = DAO.read(TEST_DATABASE.getId());
        PostgreSqlDbOptions options = (PostgreSqlDbOptions) db.getOptions();
        compareOptions(options, uOptions);
    }

}
