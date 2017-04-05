package ua.com.nov.model.dao.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.database.PostgresSqlDb;
import ua.com.nov.model.util.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PostgreSqlDatabaseDaoTest extends AbstractDatabaseDaoTest {
    public static final String URL = DbUtil.POSTGRE_SQL_LOCAL_URL;

    public static final Database TEST_DATABASE = new PostgresSqlDb(URL,"tmp",
            new PostgresSqlDb.CreateOptions.Builder().owner("postgres").encoding("UTF8")
                    .lcCollate("Russian_Russia.1251").lcType("Russian_Russia.1251").tableSpace("pg_default")
                    .connLimit(-1).allowConn(true).isTemplate(false).build());

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
        dataSource = new SingleConnectionDataSource(new PostgresSqlDb(URL, ""),
                "postgres", "postgres");
    }

    @Test
    public void testReadAll() throws DaoSystemException {
        List<Database> databases = DAO.readAll(TEST_DATABASE);
        assertTrue(databases.contains(TEST_DATABASE));
    }

    @Test
    public void testUpdateDatabase() throws DaoSystemException {
        Database updateDb = new PostgresSqlDb(URL, TEST_DATABASE.getName(),
                new PostgresSqlDb.UpdateOptions.Builder().newName("temp").owner("CURRENT_USER")
                        .connLimit(100).allowConn(true)
                        .addSetParameter("array_nulls", "off")
                        .addSetParameter("lc_messages", "false")
                        .addResetParameter("role")
                        .addResetParameter("ALL")
                        .build());
        DAO.update(updateDb);

        try (Connection conn = updateDb.getConnection(getUserName(), getPassword())){
            assertTrue(false);
        } catch (SQLException e) {/*NOP*/}
        DAO.delete(updateDb.new Id(URL, "temp"));
    }

}
