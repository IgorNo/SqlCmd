package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.table.Table;
import ua.com.nov.model.entity.table.TableID;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public abstract class AbstractTableDaoTest {

    public static final AbstractDao<TableID, Table, Database> DAO = new TableDao();
    private TableID tableID1, tableID2, tableID3;
    private Table table1, table2, table3;

    public abstract Database getTestDatabase();

    protected static void createDataSource(AbstractDatabaseDaoTest dbTest) throws SQLException {
        dbTest.setUp();
        DataSource tmpDataSource = new SingleConnectionDataSource(dbTest.getTestDatabase());
        DAO.setDataSource(tmpDataSource);
    }

    protected void createTestData(String schema) throws SQLException {
        tableID1 = new TableID(getTestDatabase(), null, schema, "table1");
        table1 = new Table(tableID1);
        DAO.create(table1);
        tableID2 = new TableID(getTestDatabase(), null, schema, "table2");
        table2 = new Table(tableID2);
        DAO.create(table2);
        tableID3 = new TableID(getTestDatabase(), null, schema, "table3");
        table3 = new Table(tableID3);
        DAO.create(table3);
    }

    @Test
    public void testCreateTable() throws SQLException {
        assertTrue(DAO.readByPK(tableID1).getPk().equals(tableID1));
    }

    @Test
    public void testReadTableByPK() throws SQLException {
        assertTrue(DAO.readByPK(tableID3).getPk().equals(tableID3));
    }

    @Test
    public void testReadAllTables() throws SQLException{
        Map<TableID, Table> tables = DAO.readAll();
        Table table = tables.get(tableID1);
        assertTrue(table1.getPk().equals(table.getPk()));
        table = tables.get(tableID2);
        assertTrue(table2.getPk().equals(table.getPk()));
        table = tables.get(tableID3);
        assertTrue(table3.getPk().equals(table.getPk()));
    }

    @Test
    public void testRenameTable() throws SQLException{
        table1.setName("table11");
        DAO.update(table1);
        tableID1.setName("table11");
        assertTrue(DAO.readByPK(tableID1).getPk().equals(tableID1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void  testDeleteTable() throws SQLException {
        DAO.delete(table2);
        DAO.readByPK(tableID2);
    }

    @Test
    public void testDeleteAllTables() throws SQLException {
        DAO.deleteAllFrom(null);
        assertTrue(DAO.readAll().size() == 0);
    }

    @After
    public void tearDown() throws SQLException{
        DAO.deleteAllFrom(null);
    }

}
