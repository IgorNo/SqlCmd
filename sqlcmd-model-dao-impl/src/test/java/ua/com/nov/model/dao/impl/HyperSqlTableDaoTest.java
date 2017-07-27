package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.MappingBusinessLogicException;
import ua.com.nov.model.dao.exception.MappingSystemException;
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
    public static void setUpClass() throws MappingSystemException, SQLException, MappingBusinessLogicException {
        HyperSqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        dataSource = new SingleConnectionDataSource(testDb, "root", "root");
        numberColumnOptions = new HyperSqlColumnOptions.Builder().autoIncrement();
        tableOptions = null;
        charColumnOptions = null;
        generatedColumnOptions = null;
        createTestData("PUBLIC", "PUBLIC", "INTEGER", "TEMPORARY", null);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException, MappingSystemException {
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
    @Test(expected = MappingSystemException.class)
    public void testReadDeleteAddUniqueKey() throws MappingSystemException {
        Table testTable = TABLE_DAO.read(customers.getId());
        UniqueKey uk = customers.getUniqueKeyList().get(0);
        readDeleteAddMetaData(UNIQUE_KEY_DAO, testTable.getUniqueKeyList().get(0).getId(), uk);
    }

    @Override
    @Test(expected = MappingBusinessLogicException.class)
    public void testRenameUniqueKey() throws MappingSystemException, MappingBusinessLogicException {
        super.testRenameUniqueKey();
    }
}
