package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.PostgesSqlDbOptions;
import ua.com.nov.model.entity.metadata.server.PostgresSqlServer;
import ua.com.nov.model.entity.metadata.server.Server;
import ua.com.nov.model.util.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PostgreSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    private static Server server = new PostgresSqlServer(DbUtil.POSTGRE_SQL_LOCAL_URL);

    public static final PostgesSqlDbOptions OPTIONS = new PostgesSqlDbOptions.Builder()
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
        DataSource dataSource = new SingleConnectionDataSource(new Database(server, ""),"postgres", "postgres");
        DAO.setDataSource(dataSource);
    }

    @Test
    public void testRead() throws DaoSystemException {
        Database db = DAO.read(TEST_DATABASE.getId());
        assertTrue(TEST_DATABASE.equals(db));
        PostgesSqlDbOptions options = (PostgesSqlDbOptions) db.getOptions();
        compareOptions(OPTIONS, options);
        assertTrue(OPTIONS.getEncoding().equalsIgnoreCase(options.getEncoding()));
        assertTrue(OPTIONS.getLcCollate().equalsIgnoreCase(options.getLcCollate()));
        assertTrue(OPTIONS.getLcType().equalsIgnoreCase(options.getLcType()));
    }

    private void compareOptions(PostgesSqlDbOptions options1, PostgesSqlDbOptions options2) {
        assertTrue(options1.getOwner().equalsIgnoreCase(options2.getOwner()));
        assertTrue(options1.getTableSpace().equalsIgnoreCase(options2.getTableSpace()));
        assertTrue(options1.getAllowConn().equals(options2.getAllowConn()));
        assertTrue(options1.isTemplate().equals(options2.isTemplate()));
        assertTrue(options1.getConnLimit().equals(options2.getConnLimit()));
    }


    @Test
    public void testReadAll() throws DaoSystemException {
        List<Database> databases = DAO.readAll(server.getId());
        assertTrue(databases.contains(TEST_DATABASE));
    }

    @Test
    public void testUpdateDatabase() throws DaoSystemException {
        PostgesSqlDbOptions uOptions = new PostgesSqlDbOptions.Builder()
                .owner("postgres").connLimit(100).allowConn(true)
                .tableSpace("pg_default").isTemplate(false)
                .build();
        Database updateDb = new Database(server, TEST_DATABASE.getName(),uOptions);
        DAO.update(updateDb);
        Database db = DAO.read(TEST_DATABASE.getId());
        PostgesSqlDbOptions options = (PostgesSqlDbOptions) db.getOptions();
        compareOptions(options, uOptions);
    }

}
