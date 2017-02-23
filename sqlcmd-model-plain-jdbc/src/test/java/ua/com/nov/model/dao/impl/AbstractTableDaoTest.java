package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.database.DataType;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.entity.table.TableId;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public abstract class AbstractTableDaoTest {

    public static final AbstractDao<Table> DAO = new TableDao();

    private static DataSource dataSource;

    private TableId tableId1, tableId2, tableId3;
    private Table table1, table2, table3;

    public abstract Database getTestDatabase();

    protected static void createDataSource(AbstractDatabaseDaoTest dbTest) throws SQLException {
        dbTest.setUp();
        dataSource  = new SingleConnectionDataSource(dbTest.getTestDatabase());
        DAO.setDataSource(dbTest.getDataSource());
    }

    protected void createTestData(String schema) throws SQLException {
        Database testDb = new DatabaseDao().readOne(getTestDatabase());
        DataType intDataType = testDb.getListDataType(Types.INTEGER).get(0);
        tableId1 = new TableId(testDb, null, schema, "table1");
        table1 = new Table(tableId1);
        table1.addColumn(new Column(1, table1, "id1", intDataType));
        DAO.create(table1);
        tableId2 = new TableId(getTestDatabase(), null, schema, "table2");
        table2 = new Table(tableId2);
        table2.addColumn(new Column(1, table2, "id2", intDataType));
        DAO.create(table2);
        tableId3 = new TableId(getTestDatabase(), null, schema, "table3");
        table3 = new Table(tableId3);
        table3.addColumn(new Column(1, table3, "id3", intDataType));
        table3 = new Table(tableId3);
        DAO.create(table3);
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {

        DAO.setDataSource(dataSource);
    }


    @Test
    public void testCreateTable() throws SQLException {
        assertTrue(DAO.readOne(table1).getId().equals(tableId1));
    }

    @Test
    public void testReadTableByPK() throws SQLException {
        assertTrue(DAO.readOne(table3).getId().equals(tableId3));
    }

    @Test
    public void testReadAllTables() throws SQLException{
        List<Table> tables = DAO.readAll(table1);
        Table table = tables.get(0);
        assertTrue(table1.getId().equals(table.getId()));
        table = tables.get(1);
        assertTrue(table2.getId().equals(table.getId()));
        table = tables.get(2);
        assertTrue(table3.getId().equals(table.getId()));
    }

    @Test
    public void testRenameTable() throws SQLException{
        table1.setName("table11");
        DAO.update(table1);
        tableId1.setName("table11");
        assertTrue(DAO.readOne(table1).getId().equals(tableId1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void  testDeleteTable() throws SQLException {
        DAO.delete(table2);
        DAO.readOne(table2);
    }

    @Test
    public void testDeleteAllTables() throws SQLException {
        DAO.deleteAll(null);
        assertTrue(DAO.readAll(null).size() == 0);
    }

    @After
    public void tearDown() throws SQLException{
        DAO.deleteAll(null);
    }

    public static void tearDownClass() throws SQLException {
        dataSource.getConnection().close();
    }

}
