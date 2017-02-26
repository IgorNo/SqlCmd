package ua.com.nov.model.dao.impl;

import org.junit.After;
import org.junit.Test;
import ua.com.nov.model.dao.AbstractDao;
import ua.com.nov.model.datasource.SingleConnectionDataSource;
import ua.com.nov.model.entity.column.Column;
import ua.com.nov.model.entity.database.DataType;
import ua.com.nov.model.entity.database.Database;
import ua.com.nov.model.entity.table.TableMetaData;
import ua.com.nov.model.entity.table.TableId;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static org.junit.Assert.assertTrue;

public abstract class AbstractTableDaoTest {

    public static final AbstractDao<TableMetaData> DAO = new TableDao();

    private static DataSource dataSource;

    private static TableId tableId1, tableId2, tableId3;
    private static TableMetaData table1, table2, table3;

    protected abstract Database getTestDatabase();

    protected static DataSource getDataSource() {
        return dataSource;
    }

    protected void createTestData(String catalog, String schema) throws SQLException {
        Database testDb = getTestDatabase();
        if (dataSource == null) {
            dataSource = new SingleConnectionDataSource(testDb);
            new DatabaseDao().setDataSource(dataSource).readOne(getTestDatabase());
            DAO.setDataSource(dataSource);
        }
        DataType intDataType = testDb.getAutoincrementalDataTypes(Types.INTEGER).get(0);

        tableId1 = new TableId(testDb, "Customers", catalog, schema);
        table1 = new TableMetaData(tableId1);
        table1.addColumn(new Column.Builder(1, table1, "id", intDataType).build());


        tableId2 = new TableId(testDb, "Table2", catalog, schema);
        table2 = new TableMetaData(tableId2);
        table2.addColumn(new Column.Builder(1, table2, "id2", intDataType).build());

        tableId3 = new TableId(testDb, "Table3", catalog, schema);
        table3 = new TableMetaData(tableId3);
        table3.addColumn(new Column.Builder(1, table3, "id3", intDataType).build());
    }


    public void setUp() throws SQLException {
        tearDown();
        DAO.create(table1);
        DAO.create(table2);
        DAO.create(table3);
    }

    @Test
    public void testReaOne() throws SQLException {
        assertTrue(DAO.readOne(table3).equals(table3));
    }

    @Test
    public void testReadAllTables() throws SQLException{
        List<TableMetaData> tables = DAO.readAll(table1);
        assertTrue(tables.contains(table1));
        assertTrue(tables.contains(table2));
        assertTrue(tables.contains(table3));
    }

    @Test
    public void testRenameTable() throws SQLException{
        table1.setName("table11");
        DAO.update(table1);
        TableMetaData updateTable = new TableMetaData(table1.getDb(), table1.getName(), table1.getCatalog(), table1.getSchema());
        assertTrue(DAO.readOne(updateTable).getName().equalsIgnoreCase(table1.getName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void  testDeleteTable() throws SQLException {
        DAO.delete(table2);
        DAO.readOne(table2);
    }

    @Test
    public void testDeleteAllTables() throws SQLException {
        TableMetaData pattern = new TableMetaData(new TableId(getTestDatabase(), "pattern"));
        DAO.deleteAll(pattern);
        assertTrue(DAO.readAll(pattern).size() == 0);
    }

    @After
    public void tearDown() throws SQLException {
        DAO.deleteAll(new TableMetaData(new TableId(getTestDatabase(), "pattern")));
    }

    protected static void tearDownClass() throws SQLException {
        dataSource.getConnection().close();
    }

}
