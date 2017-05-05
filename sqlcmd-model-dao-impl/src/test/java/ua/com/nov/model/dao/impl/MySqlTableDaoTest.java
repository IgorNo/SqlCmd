package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.metadata.database.MySqlTableOptions;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.constraint.ForeignKey;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class MySqlTableDaoTest extends AbstractTableDaoTest {
    private static final AbstractDatabaseDaoTest DATABASE_DAO_TEST = new MySqlDatabaseDaoTest();
    private static final  MySqlTableOptions options = new MySqlTableOptions.Builder()
            .checkSum(false).engine("InnoDB").avgRowLength(200).autoIncrement(100)
            .defaultCharset("utf8").collate("utf8_slovenian_ci").rowFormat("DYNAMIC")
            .createOptions("MAX_ROWS=100 MIN_ROWS=2").build();

    @BeforeClass
    public static void setUpClass() throws SQLException, DaoSystemException, DaoBusinessLogicException {
        MySqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        dataSource = new SingleConnectionDataSource(testDb, "root", "root");
        createTestData(testDb.getName(), null, "INT", "TEMPORARY", options);
    }

    @Override
    @Test
    public void testReadTableMetaData() throws DaoSystemException, DaoBusinessLogicException {
        super.testReadTableMetaData();
        Table result = TABLE_DAO.read(users.getId());
        assertTrue(users.equals(orders));
        compareColumns(result, users);
        MySqlTableOptions rOptions = (MySqlTableOptions) result.getOptions();
        compareOptions(options, rOptions);
        assertTrue(rOptions.getCreateOptions().contains("max_rows=100"));
        assertTrue(rOptions.getCreateOptions().contains("min_rows=2"));
    }

    private static void compareOptions(MySqlTableOptions options, MySqlTableOptions rOptions) {
        assertTrue(options.getEngine().equalsIgnoreCase(rOptions.getEngine()));
        assertTrue(options.getDefaultCharset().equalsIgnoreCase(rOptions.getDefaultCharset()));
        assertTrue(options.getCollation().equalsIgnoreCase(rOptions.getCollation()));
        assertTrue(options.getRowFormat().equalsIgnoreCase(rOptions.getRowFormat()));
        assertTrue(options.getCheckSum().equals(rOptions.getCheckSum()));
        assertTrue(options.getAutoIncrement().equals(rOptions.getAutoIncrement()));
        assertTrue(options.getAvgRowLength().equals(rOptions.getAvgRowLength()));
    }

    @Test
    public void testUpdateTable() throws DaoSystemException, DaoBusinessLogicException {
        MySqlTableOptions uOptions = new MySqlTableOptions.Builder()
                .checkSum(true).autoIncrement(200).avgRowLength(300).engine("MyISAM")
                .defaultCharset("cp1251").collate("cp1251_ukrainian_ci").rowFormat("FIXED").build();
        Table table = new Table.Builder(users.getId()).viewName("New Comment").options(uOptions).build();
        TABLE_DAO.update(table);
        Table result = TABLE_DAO.read(users.getId());
        MySqlTableOptions rOptions = (MySqlTableOptions) result.getOptions();
        assertTrue(result.getViewName().equalsIgnoreCase(table.getViewName()));
        compareOptions(uOptions, rOptions);
    }


    @Override
    @Test (expected = UnsupportedOperationException.class)
    public void testRenamePrimaryKey() throws DaoSystemException {
        PrimaryKey testPk = customers.getPrimaryKey();
        testPk.setNewName("test");
        PRIMARY_KEY_DAO.update(testPk);
        assertTrue(false);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testRenameForeignKey() throws DaoSystemException {
        ForeignKey testPk = orders.getForeignKeyList().get(0);
        testPk.setNewName("test");
        FOREIGN_KEY_DAO.update(testPk);
        assertTrue(false);
    }


    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }
}
