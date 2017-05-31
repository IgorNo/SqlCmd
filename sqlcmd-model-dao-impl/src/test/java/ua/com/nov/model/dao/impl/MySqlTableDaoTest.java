package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.database.ColumnOptions;
import ua.com.nov.model.entity.metadata.database.MySqlColumnOptions;
import ua.com.nov.model.entity.metadata.database.MySqlTableOptions;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class MySqlTableDaoTest extends AbstractTableDaoTest {
    private static final AbstractDatabaseDaoTest DATABASE_DAO_TEST = new MySqlDatabaseDaoTest();
    private static final MySqlTableOptions UPDATE_TABLE_OPTION = new MySqlTableOptions.Builder()
            .checkSum(true).autoIncrement(200).avgRowLength(300).engine("MyISAM")
            .defaultCharset("cp1251").collate("cp1251_ukrainian_ci").rowFormat("FIXED").build();
    public static final ColumnOptions.Builder<MySqlColumnOptions> UPDATE_COLUMN_OPTIONS =
            new MySqlColumnOptions.Builder().charSet("utf8").collation("utf8_estonian_ci");

    @BeforeClass
    public static void setUpClass() throws SQLException, DaoSystemException, DaoBusinessLogicException {
        MySqlDatabaseDaoTest.setUpClass();
        DATABASE_DAO_TEST.setUp();
        testDb = DATABASE_DAO_TEST.getTestDatabase();
        dataSource = new SingleConnectionDataSource(testDb, "root", "root");
        tableOptions = new MySqlTableOptions.Builder()
                .checkSum(false).engine("InnoDB").avgRowLength(200).autoIncrement(100)
                .defaultCharset("utf8").collate("utf8_slovenian_ci").rowFormat("DYNAMIC")
                .minRows(2).maxRows(100).build();
        numberColumnOptions = new MySqlColumnOptions.Builder().autoIncrement().zeroFill().unsigned();
        charColumnOptions = new MySqlColumnOptions.Builder().charSet("ucs2").collation("ucs2_bin").binari();
        geeratedColumnOptions = new MySqlColumnOptions.Builder()
                .generationExpression("concat('login: ',login)", MySqlColumnOptions.GenerationColumnType.VIRTUAL);
        createTestData(testDb.getName(), null, "INT", "TEMPORARY");
    }

    @Override
    protected Optional<Table> getUpdateTableOptions() {
        return UPDATE_TABLE_OPTION;
    }

    @Override
    protected ColumnOptions.Builder<MySqlColumnOptions> getUpdateColumnOptions() {
        return UPDATE_COLUMN_OPTIONS;
    }

    @Test
    @Override
    public void testReadDeleteAddPrimaryKey() throws DaoSystemException {
        Table testTable = TABLE_DAO.read(users.getId());
        TableMd.Id mdId = testTable.getPrimaryKey().getId();
        PrimaryKey md = PRIMARY_KEY_DAO.read(mdId);
        assertTrue(md.getId().equals(mdId));
        PRIMARY_KEY_DAO.delete(md);
        try {
            PRIMARY_KEY_DAO.read(mdId);
            assertTrue(false);
        } catch (DaoBusinessLogicException e) {/*NOP*/}
        PRIMARY_KEY_DAO.create(users.getPrimaryKey());
        PrimaryKey result = PRIMARY_KEY_DAO.read(testTable.getPrimaryKey().getId());
        assertTrue(result.equals(md));
    }

    @Override
    @Test (expected = DaoBusinessLogicException.class)
    public void testCreateTemporaryTable() throws DaoSystemException {
        super.testCreateTemporaryTable();
    }

    @Override
    @Test (expected = UnsupportedOperationException.class)
    public void testRenamePrimaryKey() throws DaoSystemException {
        super.testRenamePrimaryKey();
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void testRenameForeignKey() throws DaoSystemException {
        super.testRenameForeignKey();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
    }
}
