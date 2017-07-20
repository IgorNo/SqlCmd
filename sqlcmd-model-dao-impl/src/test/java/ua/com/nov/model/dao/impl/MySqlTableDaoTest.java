package ua.com.nov.model.dao.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.dao.exception.DaoBusinessLogicException;
import ua.com.nov.model.dao.exception.DaoSystemException;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.Optional;
import ua.com.nov.model.entity.metadata.database.Database;
import ua.com.nov.model.entity.metadata.schema.Schema;
import ua.com.nov.model.entity.metadata.table.MySqlTableOptions;
import ua.com.nov.model.entity.metadata.table.Table;
import ua.com.nov.model.entity.metadata.table.TableMd;
import ua.com.nov.model.entity.metadata.table.column.ColumnOptions;
import ua.com.nov.model.entity.metadata.table.column.MySqlColumnOptions;
import ua.com.nov.model.entity.metadata.table.constraint.PrimaryKey;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class MySqlTableDaoTest extends AbstractTableDaoTest {
    public static final ColumnOptions.Builder<MySqlColumnOptions> UPDATE_COLUMN_OPTIONS =
            new MySqlColumnOptions.Builder().charSet("utf8").collation("utf8_estonian_ci");
    private static final AbstractDatabaseDaoTest DATABASE_DAO_TEST = new MySqlDatabaseDaoTest();
    private static final MySqlTableOptions UPDATE_TABLE_OPTION = new MySqlTableOptions.Builder()
            .checkSum(true).autoIncrement(200).avgRowLength(300).engine("MyISAM")
            .defaultCharset("cp1251").collate("cp1251_ukrainian_ci").rowFormat("FIXED").build();

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
        numberColumnOptions = new MySqlColumnOptions.Builder().autoIncrement();
        charColumnOptions = new MySqlColumnOptions.Builder().charSet("ucs2").collation("ucs2_bin").binari();
        generatedColumnOptions = new MySqlColumnOptions.Builder()
                .generationExpression("concat('login: ',login)", MySqlColumnOptions.GenerationColumnType.VIRTUAL);
        createTestData(testDb.getName(), null, "BIGINT", "TEMPORARY", null);
        Schema.Id schemaId = new Schema.Id(testDb.getId(), null, "tmp_schema");
        testSchema = new Schema(schemaId, null);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException, DaoSystemException {
        AbstractTableDaoTest.tearDownClass();
        DATABASE_DAO_TEST.tearDown();
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

    @Test
    @Override
    public void testReadSchema() throws DaoSystemException {
        Database db = new Database(testDb.getServer(), "tmp_schema");
        Database result = new DatabaseDao(SCHEMA_DAO.getDataSource()).read(db.getId());
        assertTrue(result.equals(db));
    }

    @Test
    @Override
    public void testReadAllSchemas() throws DaoSystemException {
    }

    @Test(expected = DaoSystemException.class)
    @Override
    public void testRenameSchema() throws DaoSystemException {
        super.testRenameSchema();
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
}
