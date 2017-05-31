package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.database.ColumnOptions;
import ua.com.nov.model.entity.metadata.database.PostgresSqlColumnOptions;
import ua.com.nov.model.entity.metadata.database.PostgresSqlTableOptions;
import ua.com.nov.model.entity.metadata.table.Table;

import java.sql.SQLException;

public class PostgreSqlTableDaoTest extends AbstractTableDaoTest {
    private static final AbstractDatabaseDaoTest DATABASE_DAO_TEST = new PostgreSqlDatabaseDaoTest();
    private static final PostgresSqlTableOptions uOptions = new PostgresSqlTableOptions.Builder()
            .tableSpace("pg_default").oids(true).addStorageParameter("fillfactor", "95")
            .addStorageParameter("autovacuum_enabled", "false").owner("postgres")
            .build();


    @BeforeClass
    public static void setUpClass() throws SQLException, DaoSystemException, DaoBusinessLogicException {
        PostgreSqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        dataSource = new SingleConnectionDataSource(testDb, "postgres", "postgres");
        tableOptions = new PostgresSqlTableOptions.Builder()
                .tableSpace("pg_default").oids(false).addStorageParameter("fillfactor", "75")
                .addStorageParameter("autovacuum_enabled", "true")
                .build();
        numberColumnOptions = new PostgresSqlColumnOptions.Builder().autoIncrement();
        createTestData(null, "public", "serial", null);
    }

    @Override
    protected Optional<Table> getUpdateTableOptions() {
        return uOptions;
    }

    @Override
    protected ColumnOptions.Builder<? extends ColumnOptions> getUpdateColumnOptions() {
        return null;
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }
}
