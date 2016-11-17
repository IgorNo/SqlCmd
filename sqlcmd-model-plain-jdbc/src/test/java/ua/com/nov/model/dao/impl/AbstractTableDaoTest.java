package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Test;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.entity.Database;
import ua.com.nov.model.entity.Table;
import ua.com.nov.model.entity.TablePK;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public abstract class AbstractTableDaoTest {

    public static final AbstractDao<TablePK, Table, Database> DAO = new TableDao();
    private TablePK tablePK1, tablePK2, tablePK3;
    private Table table1, table2, table3;

    public abstract Database getTestDatabase();

    protected static void createDataSource(AbstractDatabaseDaoTest dbTest) throws SQLException {
        dbTest.setUp();
        DataSource tmpDataSource = new SingleConnectionDataSource(dbTest.getTestDatabase());
        DAO.setDataSource(tmpDataSource);
    }

    protected void createTestData(String schema) throws SQLException {
        tablePK1 = new TablePK(getTestDatabase(), null, schema, "table1");
        table1 = new Table(tablePK1);
        DAO.create(table1);
        tablePK2 = new TablePK(getTestDatabase(), null, schema, "table2");
        table2 = new Table(tablePK2);
        DAO.create(table2);
        tablePK3 = new TablePK(getTestDatabase(), null, schema, "table3");
        table3 = new Table(tablePK3);
        DAO.create(table3);
    }

    @Test
    public void testCreateTable() throws SQLException {
        assertTrue(DAO.readByPK(tablePK1).getPk().equals(tablePK1));
    }

    @Test
    public void testReadTableByPK() throws SQLException {
        assertTrue(DAO.readByPK(tablePK3).getPk().equals(tablePK3));
    }

    @Test
    public void testReadAllTables() throws SQLException{
        Map<TablePK, Table> tables = DAO.readAll();
        Table table = tables.get(tablePK1);
        assertTrue(table1.getPk().equals(table.getPk()));
        table = tables.get(tablePK2);
        assertTrue(table2.getPk().equals(table.getPk()));
        table = tables.get(tablePK3);
        assertTrue(table3.getPk().equals(table.getPk()));
    }

    @Test
    public void testRenameTable() throws SQLException{
        table1.setName("table11");
        DAO.update(table1);
        tablePK1.setName("table11");
        assertTrue(DAO.readByPK(tablePK1).getPk().equals(tablePK1));
    }

    @Test(expected = SQLException.class)
    public void  testDeleteTable() throws SQLException {
        DAO.delete(table2);
        DAO.readByPK(tablePK2);
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
