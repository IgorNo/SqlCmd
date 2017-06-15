package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.table.PostgreSqlTableOptions;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.ColumnOptions;
import ua.com.nov.model.entity.metadata.table.column.PostgresSqlColumnOptions;

import java.sql.SQLException;

public class PostgreSqlTableDaoTest extends AbstractTableDaoTest {
    private static final AbstractDatabaseDaoTest DATABASE_DAO_TEST = new PostgreSqlDatabaseDaoTest();
    private static final PostgreSqlTableOptions uOptions = new PostgreSqlTableOptions.Builder()
            .tableSpace("pg_default").oids(true).addStorageParameter("fillfactor", "95")
            .addStorageParameter("autovacuum_enabled", "false").owner("postgres")
            .build();


    @BeforeClass
    public static void setUpClass() throws SQLException, DaoSystemException, DaoBusinessLogicException {
        PostgreSqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        dataSource = new SingleConnectionDataSource(testDb, "postgres", "postgres");
        tableOptions = new PostgreSqlTableOptions.Builder()
                .tableSpace("pg_default").oids(false).addStorageParameter("fillfactor", "75")
                .addStorageParameter("autovacuum_enabled", "true")
                .build();
        numberColumnOptions = new PostgresSqlColumnOptions.Builder().autoIncrement();
        charColumnOptions = null;
        generatedColumnOptions = null;
        createTestData(null, "public", "serial", null, null);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException, DaoSystemException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }

    @Override
    protected Optional<Table> getUpdateTableOptions() {
        return uOptions;
    }

    @Override
    protected ColumnOptions.Builder<? extends ColumnOptions> getUpdateColumnOptions() {
        return null;
    }
}
