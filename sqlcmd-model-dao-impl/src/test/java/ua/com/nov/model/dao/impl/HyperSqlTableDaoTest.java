package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.column.ColumnOptions;
import ua.com.nov.model.entity.metadata.table.column.HyperSqlColumnOptions;
import ua.com.nov.model.entity.metadata.table.constraint.UniqueKey;

import java.sql.SQLException;

public class HyperSqlTableDaoTest extends AbstractTableDaoTest {
    private static AbstractDatabaseDaoTest DATABASE_DAO_TEST = new HyperSqlDatabaseDaoTest();

    @BeforeClass
    public static void setUpClass() throws DaoSystemException, SQLException, DaoBusinessLogicException {
        HyperSqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        dataSource = new SingleConnectionDataSource(testDb, "root", "root");
        numberColumnOptions = new HyperSqlColumnOptions.Builder().autoIncrement();
        charColumnOptions = null;
        generatedColumnOptions = null;
        createTestData(null, "PUBLIC", "INTEGER", "TEMPORARY", null);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException, DaoSystemException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }

    @Override
    protected Optional<Table> getUpdateTableOptions() {
        return null;
    }

    @Override
    protected ColumnOptions.Builder<HyperSqlColumnOptions> getUpdateColumnOptions() {
        return null;
    }

    @Override
    @Test(expected = DaoSystemException.class)
    public void testReadDeleteAddUniqueKey() throws DaoSystemException {
        Table testTable = TABLE_DAO.read(customers.getId());
        UniqueKey uk = customers.getUniqueKeyList().get(0);
        readDeleteAddMetaData(UNIQUE_KEY_DAO, testTable.getUniqueKeyList().get(0).getId(), uk);
    }

    @Override
    @Test(expected = DaoBusinessLogicException.class)
    public void testRenameUniqueKey() throws DaoSystemException, DaoBusinessLogicException {
        super.testRenameUniqueKey();
    }
}
